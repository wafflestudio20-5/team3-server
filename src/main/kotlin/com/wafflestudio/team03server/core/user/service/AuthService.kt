package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.*
import com.wafflestudio.team03server.core.user.api.request.ResetPasswordRequest
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.entity.Coordinate
import com.wafflestudio.team03server.core.user.repository.UserRepository
import com.wafflestudio.team03server.utils.EmailService
import com.wafflestudio.team03server.utils.RedisUtil
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTReader
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
    fun refresh(refreshToken: String): AuthToken
    fun resetPassword(resetPasswordRequest: ResetPasswordRequest)
    fun coordinateToPoint(coordinate: Coordinate): Point
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
        if (signUpRequest.isEmailAuthed == false) {
            throw Exception401("이메일 인증이 되지 않은 사용자 입니다.")
        }
        val point = coordinateToPoint(signUpRequest.coordinate!!)
        val user = signUpRequest.toUser(point)
        user.password = passwordEncoder.encode(user.password)
        val (accessToken, refreshToken) = authTokenService.generateAccessTokenAndRefreshToken(signUpRequest.email, user)
        userRepository.save(user)
        return LoginResponse(accessToken, refreshToken, SimpleUserResponse.of(user))
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
        val (accessToken, refreshToken) = authTokenService.generateAccessTokenAndRefreshToken(email, findUser)
        return LoginResponse(accessToken, refreshToken, SimpleUserResponse.of(findUser))
    }

    private fun generateRandomKey(): Int {
        val random = Random()
        return random.nextInt(900000) + 100000
    }

    override fun refresh(refreshToken: String): AuthToken {
        authTokenService.verifyToken(refreshToken, isRefreshToken = true)
        val email = authTokenService.getCurrentUserEmail(refreshToken)
        val user = userRepository.findByEmail(email) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        return authTokenService.generateAccessTokenAndRefreshToken(email, user)
    }

    override fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        val user = userRepository.findByEmail(resetPasswordRequest.email) ?: throw Exception404("존재하지 않는 이메일입니다.")
        if (!resetPasswordRequest.isEmailAuthed) {
            throw Exception401("이메일 인증이 되지 않은 사용자 입니다.")
        }
        if (resetPasswordRequest.newPassword != resetPasswordRequest.newPasswordConfirm) {
            throw Exception400("비밀번호가 일치하지 않습니다.")
        }
        user.password = passwordEncoder.encode(resetPasswordRequest.newPassword)
    }

    override fun coordinateToPoint(coordinate: Coordinate): Point {
        val pointWKT = "POINT(${coordinate.lng} ${coordinate.lat})"
        return WKTReader().read(pointWKT) as Point
    }
}
