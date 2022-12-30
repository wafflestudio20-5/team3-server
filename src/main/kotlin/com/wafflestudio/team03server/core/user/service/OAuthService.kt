package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.core.user.controller.response.GoogleLoginResponse
import com.wafflestudio.team03server.core.user.controller.vo.GoogleAuthToken
import com.wafflestudio.team03server.core.user.controller.vo.GoogleUser
import com.wafflestudio.team03server.utils.GoogleOAuth
import org.springframework.stereotype.Service

@Service
class OAuthService(
    private val googleOAuth: GoogleOAuth,
) {

    fun oAuthLogin(code: String): GoogleLoginResponse {
        val googleAuthToken: GoogleAuthToken = googleOAuth.getAccessToken(code)
        val googleUser: GoogleUser = googleOAuth.getUserInfo(googleAuthToken)
        val userEmail: String = googleUser.email
        return GoogleLoginResponse(
            "zxcuasc.asxauisx.asdxas",
            100,
            googleAuthToken.access_token,
            googleAuthToken.token_type
        )
    }
}
