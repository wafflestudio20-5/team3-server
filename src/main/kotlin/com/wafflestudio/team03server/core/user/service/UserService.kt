package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception401
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

interface UserService {
    fun signUp(signUpRequest: SignUpRequest)
    fun checkDuplicatedEmail(email: String): Boolean
    fun checkDuplicateUsername(username: String): Boolean
    fun verifyEmail(token: String): ResponseEntity<Any>
    fun login(email: String, password: String)
}

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
) : UserService {

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

    override fun login(email: String, password: String) {
        val user = userRepository.findByEmail(email) ?: throw Exception404("No exsisting user with email: ${email}")
        if (!passwordEncoder.matches(password, user.password)) {
            throw Exception401("Incorrect password")
        }
    }

    private fun generateVerificationToken(): String {
        return UUID.randomUUID().toString()
    }
}
