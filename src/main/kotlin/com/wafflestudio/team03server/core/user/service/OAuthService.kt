package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.user.api.response.GoogleLoginResponse
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import com.wafflestudio.team03server.utils.GoogleOAuth
import org.springframework.stereotype.Service

private const val NEED_SIGNUP_MESSAGE = "회원가입이 필요합니다."

@Service
class OAuthService(
    private val googleOAuth: GoogleOAuth,
    private val userRepository: UserRepository,
) {

//    fun oAuthLogin(code: String): GoogleLoginResponse {
//        val googleAuthToken: GoogleAuthToken = googleOAuth.getAccessToken(code)
//        val googleUser: GoogleUser = googleOAuth.getUserInfo(googleAuthToken)
//        val userEmail: String = googleUser.email
//        return GoogleLoginResponse(
//            "zxcuasc.asxauisx.asdxas",
//            100,
//            googleAuthToken.access_token,
//            googleAuthToken.token_type
//        )
//    }

    fun googleLogin(email: String): GoogleLoginResponse {
        val findUser = userRepository.findByEmail(email) ?: throw Exception404(NEED_SIGNUP_MESSAGE)
        if (!findUser.emailVerified) {
            userRepository.delete(findUser)
            throw Exception404(NEED_SIGNUP_MESSAGE)
        }
        return GoogleLoginResponse(SimpleUserResponse.of(findUser))
    }
}
