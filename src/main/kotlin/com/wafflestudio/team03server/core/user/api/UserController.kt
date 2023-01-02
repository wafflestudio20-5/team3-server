package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.user.api.request.EditPasswordRequest
import com.wafflestudio.team03server.core.user.api.request.EditProfileRequest
import com.wafflestudio.team03server.core.user.api.response.UserResponse
import com.wafflestudio.team03server.core.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{user-id}")
    fun getUser(@PathVariable("user-id") userId: Long): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.getProfile(userId),HttpStatus.OK)
    }

    @Authenticated
    @GetMapping("/me")
    fun getMe(@UserContext userId: Long): ResponseEntity<UserResponse>{
        return ResponseEntity(userService.getProfile(userId),HttpStatus.OK)
    }

    @Authenticated
    @PatchMapping("/me")
    fun editMe(@UserContext userId: Long, @Valid @RequestBody editProfileRequest: EditProfileRequest): ResponseEntity<UserResponse>{
        return ResponseEntity(userService.editProfile(userId,editProfileRequest),HttpStatus.OK)
    }

    @Authenticated
    @PutMapping("/me/password")
    fun editPassword(@UserContext userId:Long,@Valid @RequestBody editPasswordRequest: EditPasswordRequest ):ResponseEntity<Any>{
        userService.editPassword(userId,editPasswordRequest)
        return ResponseEntity(HttpStatus.OK)
    }
}
