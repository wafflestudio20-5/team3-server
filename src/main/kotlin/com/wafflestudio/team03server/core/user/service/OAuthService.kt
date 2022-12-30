package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.user.api.response.GoogleLoginResponse
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.stereotype.Service

private const val NEED_SIGNUP_MESSAGE = "회원가입이 필요합니다."

@Service
class OAuthService(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
) {

    fun googleLogin(email: String): GoogleLoginResponse {
        val findUser = userRepository.findByEmail(email) ?: throw Exception404(NEED_SIGNUP_MESSAGE)
        checkVerifiedUser(findUser)
        val jwtToken = authTokenService.generateTokenByEmail(findUser.email).accessToken
        return GoogleLoginResponse(jwtToken, SimpleUserResponse.of(findUser))
    }

    private fun checkVerifiedUser(findUser: User) {
        if (!findUser.emailVerified) {
            userRepository.delete(findUser)
            throw Exception404(NEED_SIGNUP_MESSAGE)
        }
    }
}
