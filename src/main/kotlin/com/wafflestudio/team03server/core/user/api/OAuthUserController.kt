package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.core.user.api.request.GoogleLoginRequest
import com.wafflestudio.team03server.core.user.api.response.GoogleLoginResponse
import com.wafflestudio.team03server.core.user.service.OAuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class OAuthUserController(
    private val oAuthService: OAuthService,
) {

    @PostMapping("/google/login")
    fun googleLogin(
        @Valid @RequestBody
        googleLoginRequest: GoogleLoginRequest,
    ): ResponseEntity<GoogleLoginResponse> {
        val userEmail = googleLoginRequest.email
        val response = oAuthService.googleLogin(userEmail)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
