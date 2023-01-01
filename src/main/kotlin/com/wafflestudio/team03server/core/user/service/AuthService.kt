package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

interface AuthService {
    fun signUp(signUpRequest: SignUpRequest)
    fun checkDuplicatedEmail(email: String): Boolean
    fun checkDuplicateUsername(username: String): Boolean
    fun verifyEmail(token: String): ResponseEntity<Any>
    fun login(email: String, password: String): LoginResponse
}

@Service
@Transactional
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val authTokenService: AuthTokenService
) : AuthService {

    override fun signUp(signUpRequest: SignUpRequest) {
        checkDuplicatedEmail(signUpRequest.email!!)
        checkDuplicateUsername(signUpRequest.username!!)
        val user = signUpRequest.toUser()
        user.password = passwordEncoder.encode(user.password)
        // 인증 토큰 생성
        val verificationToken = generateVerificationToken()
        user.verificationToken = verificationToken
        userRepository.save(user)
        emailService.sendEmail(
            signUpRequest.email,
            "회원가입 인증 이메일",
            "http://localhost:8080/auth/verifyEmail?token=$verificationToken",
        )
    }

    override fun checkDuplicatedEmail(email: String): Boolean {
        return userRepository.findByEmail(email) == null
    }

    override fun checkDuplicateUsername(username: String): Boolean {
        return userRepository.findByUsername(username) == null
    }

    override fun verifyEmail(token: String): ResponseEntity<Any> {
        val user = userRepository.findByVerificationToken(token) ?: return ResponseEntity.notFound().build()
        val diff = Duration.between(user.createdAt, LocalDateTime.now())
        // 30분 초과시 토큰 무효화
        if (diff.toSeconds() > 1800) {
            return ResponseEntity.badRequest().build()
        }

        user.emailVerified = true

        return ResponseEntity.ok().build()
    }

    override fun login(email: String, password: String): LoginResponse {
        val findUser = userRepository.findByEmail(email) ?: throw Exception403("이메일 또는 비밀번호가 잘못되었습니다.")
        if (!passwordEncoder.matches(password, findUser.password)) {
            throw Exception403("이메일 또는 비밀번호가 잘못되었습니다.")
        }
        if (!findUser.emailVerified) {
            throw Exception400("이메일 인증이 필요합니다.")
        }
        val accessToken = authTokenService.generateTokenByEmail(email).accessToken
        return LoginResponse(accessToken, SimpleUserResponse.of(findUser))
    }

    private fun generateVerificationToken(): String {
        return UUID.randomUUID().toString()
    }
}
