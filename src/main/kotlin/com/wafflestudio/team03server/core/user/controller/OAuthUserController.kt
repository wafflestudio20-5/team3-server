package com.wafflestudio.team03server.core.user.controller

import com.wafflestudio.team03server.core.user.controller.response.GoogleLoginResponse
import com.wafflestudio.team03server.core.user.service.OAuthService
import com.wafflestudio.team03server.utils.GoogleOAuth
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/app/accounts/auth")
class OAuthUserController(
    private val googleOAuth: GoogleOAuth,
    private val oAuthService: OAuthService,
    private val response: HttpServletResponse,
) {

    @GetMapping("/google")
    fun googleLogin() {
        val redirectUrl: String = googleOAuth.getOAuthRedirectUrl()
        response.sendRedirect(redirectUrl)
    }

    @GetMapping("/google/callback")
    fun googleLoginCallback(
        @RequestParam(name = "code") code: String
    ): GoogleLoginResponse
    {
        return oAuthService.oAuthLogin(code)
    }

}