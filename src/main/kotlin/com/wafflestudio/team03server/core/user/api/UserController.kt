package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.user.api.response.UserResponse
import com.wafflestudio.team03server.core.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{user-id}")
    fun getUser(@PathVariable("user-id") userId: Long): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.getUser(userId),HttpStatus.OK)
    }

    @Authenticated
    @GetMapping("/me")
    fun getMe(@UserContext userId: Long): ResponseEntity<UserResponse>{
        return ResponseEntity(userService.getUser(userId),HttpStatus.OK)
    }
}
