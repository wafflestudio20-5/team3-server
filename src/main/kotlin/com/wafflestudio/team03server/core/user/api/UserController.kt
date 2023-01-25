package com.wafflestudio.team03server.core.user.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.trade.api.response.PostListResponse
import com.wafflestudio.team03server.core.user.api.request.EditLocationRequest
import com.wafflestudio.team03server.core.user.api.request.EditPasswordRequest
import com.wafflestudio.team03server.core.user.api.request.EditUsernameRequest
import com.wafflestudio.team03server.core.user.api.response.MyChatsResponse
import com.wafflestudio.team03server.core.user.api.response.UserResponse
import com.wafflestudio.team03server.core.user.service.UserService
import org.apache.commons.io.FilenameUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/{user-id}")
    fun getUser(@PathVariable("user-id") userId: Long): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.getProfile(userId), HttpStatus.OK)
    }

    @Authenticated
    @GetMapping("/me")
    fun getMe(@UserContext userId: Long): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.getProfile(userId), HttpStatus.OK)
    }

    @Authenticated
    @PatchMapping("/me/username")
    fun editUsername(
        @UserContext userId: Long,
        @Valid @RequestBody editUsernameRequest: EditUsernameRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.editUsername(userId, editUsernameRequest), HttpStatus.OK)
    }

    @Authenticated
    @PatchMapping("/me/location")
    fun editLocation(
        @UserContext userId: Long,
        @Valid @RequestBody editLocationRequest: EditLocationRequest
    ): ResponseEntity<UserResponse> {
        return ResponseEntity(userService.editLocation(userId, editLocationRequest), HttpStatus.OK)
    }

    @Authenticated
    @PatchMapping("/me/password")
    fun editPassword(
        @UserContext userId: Long,
        @Valid @RequestBody editPasswordRequest: EditPasswordRequest
    ): ResponseEntity<Any> {
        userService.editPassword(userId, editPasswordRequest)
        return ResponseEntity(HttpStatus.OK)
    }

    @Authenticated
    @PatchMapping("/me/image")
    fun uploadProfileImage(@UserContext userId: Long, @RequestParam("image") image: MultipartFile): String {
        if (!isFileAnImage(image)) {
            throw Exception400("허용되지 않는 파일 형식입니다.")
        }
        return userService.uploadImage(userId, image)
    }

    private fun isFileAnImage(file: MultipartFile): Boolean {
        val fileExtension = FilenameUtils.getExtension(file.originalFilename).lowercase()
        return fileExtension == "jpg" || fileExtension == "jpeg" || fileExtension == "png"
    }

    @Authenticated
    @GetMapping("/buy-trade")
    fun getBuyTradePosts(@UserContext userId: Long): PostListResponse {
        return userService.getBuyTradePosts(userId)
    }

    @Authenticated
    @GetMapping("/{user-id}/sell-trade")
    fun getSellTradePosts(@UserContext userId: Long, @PathVariable(name = "user-id") sellerId: Long): PostListResponse {
        return userService.getSellTradePosts(userId, sellerId)
    }

    @Authenticated
    @GetMapping("/like-trade")
    fun getLikeTradePosts(@UserContext userId: Long): PostListResponse {
        return userService.getLikeTradePosts(userId)
    }

    @Authenticated
    @GetMapping("/chats")
    fun getMyChats(@UserContext userId: Long): MyChatsResponse {
        return userService.getMyChats(userId)
    }
}
