package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.core.user.api.request.GoogleLoginRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.service.OAuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
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
    ): ResponseEntity<LoginResponse> {
        val userEmail = googleLoginRequest.email
        val response = oAuthService.googleLogin(userEmail)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/kakao/login")
    fun kakaoLogin(@RequestParam(name = "code") code: String): LoginResponse {
        return oAuthService.kakaoLogin(code)
    }
}
