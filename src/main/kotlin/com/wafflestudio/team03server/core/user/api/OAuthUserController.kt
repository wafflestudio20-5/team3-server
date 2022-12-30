package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.core.user.api.request.GoogleLoginRequest
import com.wafflestudio.team03server.core.user.api.response.GoogleLoginResponse
import com.wafflestudio.team03server.core.user.service.AuthTokenService
import com.wafflestudio.team03server.core.user.service.OAuthService
import com.wafflestudio.team03server.utils.GoogleOAuth
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
class OAuthUserController(
    private val googleOAuth: GoogleOAuth,
    private val oAuthService: OAuthService,
    private val authTokenService: AuthTokenService,
    private val response: HttpServletResponse,
) {
    val AUTHORIZATION_HEADER: String = "Authorization"

//    @GetMapping("/app/accounts/auth/google")
//    fun googleLogin() {
//        val redirectUrl: String = googleOAuth.getOAuthRedirectUrl()
//        response.sendRedirect(redirectUrl)
//    }
//
//    @GetMapping("/app/accounts/auth/google/callback")
//    fun googleLoginCallback(@RequestParam(name = "code") code: String): GoogleLoginResponse {
//        return oAuthService.oAuthLogin(code)
//    }

    @PostMapping("/google/login")
    fun googleLogin(@Valid @RequestBody googleLoginRequest: GoogleLoginRequest): ResponseEntity<GoogleLoginResponse> {
        val userEmail = googleLoginRequest.email
        val response = oAuthService.googleLogin(userEmail)
        val jwtToken = authTokenService.generateTokenByEmail(userEmail).accessToken
        val headers = HttpHeaders()
        headers.set(AUTHORIZATION_HEADER, jwtToken)
        return ResponseEntity(response, headers, HttpStatus.OK)
    }
}
