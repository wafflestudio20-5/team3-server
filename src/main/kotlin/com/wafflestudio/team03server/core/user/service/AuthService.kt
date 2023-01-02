package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.common.Exception409
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

interface AuthService {
    fun signUp(signUpRequest: SignUpRequest)
    fun sendVerificationMail(email: String)
    fun checkEmailVerified(email: String):Boolean
    fun isDuplicateEmail(email: String): Boolean
    fun isDuplicateUsername(username: String): Boolean
    fun verifyEmail(token: String)
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
        if(isDuplicateEmail(signUpRequest.email!!)){
            throw Exception409("이미 존재하는 이메일 입니다.")
        }
        if(isDuplicateUsername(signUpRequest.username!!)){
            throw Exception409("이미 존재하는 유저네임 입니다.")
        }
        val user = signUpRequest.toUser()
        user.password = passwordEncoder.encode(user.password)
        // 인증 토큰 생성
        val verificationToken = generateVerificationToken()
        user.verificationToken = verificationToken
        userRepository.save(user)
        emailService.sendVerificationEmail(signUpRequest.email, verificationToken)
    }

    override fun sendVerificationMail(email: String) {
        val user = userRepository.findByEmail(email) ?: throw Exception404("유저를 찾을 수 없습니다.")
        if (user.emailVerified){
            throw Exception400("이미 인증 완료된 이메일입니다.")
        }
        val verificationToken = generateVerificationToken()
        user.verificationToken = verificationToken
        emailService.sendVerificationEmail(email,verificationToken)
    }

    override fun checkEmailVerified(email: String): Boolean {
        val user = userRepository.findByEmail(email) ?: throw Exception404("유저를 찾을 수 없습니다.")
        return user.emailVerified
    }

    override fun isDuplicateEmail(email: String): Boolean {
        return userRepository.findByEmail(email) != null
    }

    override fun isDuplicateUsername(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }

    override fun verifyEmail(token: String){
        val user = userRepository.findByVerificationToken(token) ?: throw Exception404("토큰이 유효하지 않습니다.")
        if (user.emailVerified){
            throw Exception403("이미 인증 완료된 이메일입니다.")
        }
        val diff = Duration.between(user.modifiedAt, LocalDateTime.now())
        // 토큰 발급 후 30분 초과시 토큰 무효화
        if (diff.toSeconds() > 1800) {
            throw Exception400("인증 시간 초과")
        }

        user.emailVerified = true
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
