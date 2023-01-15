package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.SocialLoginNotFoundException
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import com.wafflestudio.team03server.utils.KakaoOAuth
import org.springframework.stereotype.Service

private const val NEED_SIGNUP_MESSAGE = "회원가입이 필요합니다."

@Service
class OAuthService(
    private val userRepository: UserRepository,
    private val authTokenService: AuthTokenService,
    private val kakaoOAuth: KakaoOAuth,
) {

    fun googleLogin(email: String): LoginResponse {
        val (findUser, jwtToken) = getUserAndJwtTokenByEmail(email)
        return LoginResponse(jwtToken.accessToken, jwtToken.refreshToken, SimpleUserResponse.of(findUser))
    }

    fun kakaoLogin(code: String): LoginResponse {
        val accessToken: String = kakaoOAuth.getKaKaoAccessToken(code)
        val (connected_at, _, kakaoUserAccount) = kakaoOAuth.getKakaoUserInfo(accessToken)
        val (findUser, jwtToken) = getUserAndJwtTokenByEmail(kakaoUserAccount.email)
        return LoginResponse(jwtToken.accessToken, jwtToken.refreshToken, SimpleUserResponse.of(findUser))
    }

    private fun getUserAndJwtTokenByEmail(email: String): Pair<User, AuthToken> {
        val findUser = userRepository.findByEmail(email) ?: throw SocialLoginNotFoundException(
            NEED_SIGNUP_MESSAGE,
            email,
        )
        val authToken = authTokenService.generateAccessTokenAndRefreshToken(findUser.email)
        return Pair(findUser, authToken)
    }
}
