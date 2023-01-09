package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception409
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import com.wafflestudio.team03server.utils.EmailService
import com.wafflestudio.team03server.utils.RedisUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface AuthService {
    fun signUp(signUpRequest: SignUpRequest): LoginResponse
    fun sendVerificationEmail(email: String)
    fun isDuplicateEmail(email: String): Boolean
    fun isDuplicateUsername(username: String): Boolean
    fun verifyEmail(code: String, email: String): Boolean
    fun login(email: String, password: String): LoginResponse
}

@Service
@Transactional
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val authTokenService: AuthTokenService,
    private val redisUtil: RedisUtil
) : AuthService {

    override fun signUp(signUpRequest: SignUpRequest): LoginResponse {
        if (isDuplicateEmail(signUpRequest.email!!)) {
            throw Exception409("이미 존재하는 이메일 입니다.")
        }
        if (isDuplicateUsername(signUpRequest.username!!)) {
            throw Exception409("이미 존재하는 유저네임 입니다.")
        }
        val user = signUpRequest.toUser()
        user.password = passwordEncoder.encode(user.password)
        userRepository.save(user)
        val accessToken = authTokenService.generateTokenByEmail(signUpRequest.email).accessToken
        return LoginResponse(accessToken, SimpleUserResponse.of(user))
    }

    override fun sendVerificationEmail(email: String) {
        val code = generateRandomKey().toString()
        redisUtil.setDataExpire(email, code, 600000) // 10분
        emailService.sendVerificationEmail(email, code)
    }

    override fun isDuplicateEmail(email: String): Boolean {
        return userRepository.findByEmail(email) != null
    }

    override fun isDuplicateUsername(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }

    override fun verifyEmail(code: String, email: String): Boolean {
        return code == redisUtil.getData(email)
    }

    override fun login(email: String, password: String): LoginResponse {
        val findUser = userRepository.findByEmail(email) ?: throw Exception403("이메일 또는 비밀번호가 잘못되었습니다.")
        if (!passwordEncoder.matches(password, findUser.password)) {
            throw Exception403("이메일 또는 비밀번호가 잘못되었습니다.")
        }
        val accessToken = authTokenService.generateTokenByEmail(email).accessToken
        return LoginResponse(accessToken, SimpleUserResponse.of(findUser))
    }

    private fun generateRandomKey(): Int {
        val random = Random()
        return random.nextInt(900000) + 100000
    }
}
