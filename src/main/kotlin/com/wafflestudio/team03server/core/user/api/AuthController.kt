package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.core.user.api.request.LoginRequest
import com.wafflestudio.team03server.core.user.api.request.RefreshRequest
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.service.AuthService
import com.wafflestudio.team03server.core.user.service.AuthToken
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    // 이메일 중복체크
    @GetMapping("/checkEmail")
    fun checkEmail(@RequestParam("email") email: String): ResponseEntity<Boolean> {
        return ResponseEntity(!authService.isDuplicateEmail(email), HttpStatus.OK)
    }

    // 유저네임 중복체크
    @GetMapping("/checkUsername")
    fun checkUsername(@RequestParam("username") username: String): ResponseEntity<Boolean> {
        return ResponseEntity(!authService.isDuplicateUsername(username), HttpStatus.OK)
    }

    // 회원가입
    @PostMapping("/signup")
    fun signUp(
        @Valid @RequestBody
        signUpRequest: SignUpRequest,
    ): ResponseEntity<LoginResponse> {
        val response = authService.signUp(signUpRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }

    // 인증 이메일 전송
    @GetMapping("/sendVerificationEmail")
    fun sendVerificationEmail(@RequestParam email: String): ResponseEntity<Any> {
        authService.sendVerificationEmail(email)
        return ResponseEntity(HttpStatus.OK)
    }

    // 이메일 인증
    @GetMapping("/verifyEmail")
    fun verifyEmail(@RequestParam("code") code: String, @RequestParam("email") email: String): ResponseEntity<Boolean> {
        return ResponseEntity(authService.verifyEmail(code, email), HttpStatus.OK)
    }

    // 로그인
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.login(loginRequest.email, loginRequest.password)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody @Valid refreshRequest: RefreshRequest): ResponseEntity<AuthToken> {
        val authToken = authService.refresh(refreshRequest.refreshToken!!)
        return ResponseEntity(authToken, HttpStatus.OK)
    }
}
