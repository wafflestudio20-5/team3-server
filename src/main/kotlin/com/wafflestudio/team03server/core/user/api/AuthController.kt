package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.core.user.api.request.LoginRequest
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.service.AuthService
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
    ): ResponseEntity<Any> {
        authService.signUp(signUpRequest)
        return ResponseEntity(HttpStatus.OK)
    }

    // 인증 이메일 전송
    @GetMapping("/sendVerificationEmail")
    fun sendVerificationEmail(@RequestParam email: String): ResponseEntity<Any> {
        authService.sendVerificationMail(email)
        return ResponseEntity(HttpStatus.OK)
    }

    // 이메일 인증 확인
    @GetMapping("/checkEmailVerified")
    fun checkEmailVerified(@RequestParam email: String): ResponseEntity<Boolean> {
        return ResponseEntity(authService.checkEmailVerified(email), HttpStatus.OK)
    }

    // 이메일 인증
    @GetMapping("/verifyEmail")
    fun verifyEmail(@RequestParam("token") token: String): ResponseEntity<Any> {
        authService.verifyEmail(token)
        return ResponseEntity(HttpStatus.OK)
    }

    // 로그인
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.login(loginRequest.email, loginRequest.password)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
