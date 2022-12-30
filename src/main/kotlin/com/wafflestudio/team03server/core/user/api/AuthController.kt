package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService
) {
    // 이메일 중복체크
    @PostMapping("/checkEmail")
    fun checkEmail(@RequestParam("email") email: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok(userService.checkDuplicatedEmail(email))
    }

    // 유저네임 중복체크
    @PostMapping("/checkUsername")
    fun checkUsername(@RequestParam("username") username: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok(userService.checkDuplicateUsername(username))
    }

    // 회원가입
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<Any> {
        userService.signUp(signUpRequest)
        return ResponseEntity.ok().build()
    }

    // 이메일 인증
    @GetMapping("/verifyEmail")
    fun verifyEmail(@RequestParam("token") token: String): ResponseEntity<Any> {
        return userService.verifyEmail(token)
    }
}
