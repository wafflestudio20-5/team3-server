package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.core.user.api.request.LoginRequest
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.api.response.LoginResponse
import com.wafflestudio.team03server.core.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
) {
    // 이메일 중복체크
    @PostMapping("/checkEmail")
    fun checkEmail(@RequestParam("email") email: String): ResponseEntity<Boolean> {
        return ResponseEntity(userService.checkDuplicatedEmail(email), HttpStatus.OK)
    }

    // 유저네임 중복체크
    @PostMapping("/checkUsername")
    fun checkUsername(@RequestParam("username") username: String): ResponseEntity<Boolean> {
        return ResponseEntity(userService.checkDuplicateUsername(username), HttpStatus.OK)
    }

    // 회원가입
    @PostMapping("/signup")
    fun signUp(
        @Valid @RequestBody
        signUpRequest: SignUpRequest,
    ): ResponseEntity<Any> {
        userService.signUp(signUpRequest)
        return ResponseEntity(HttpStatus.OK)
    }

    // 이메일 인증
    @GetMapping("/verifyEmail")
    fun verifyEmail(@RequestParam("token") token: String): ResponseEntity<Any> {
        return userService.verifyEmail(token)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val response = userService.login(loginRequest.email!!, loginRequest.password!!)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
