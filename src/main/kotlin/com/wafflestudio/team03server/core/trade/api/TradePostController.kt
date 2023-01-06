package com.wafflestudio.team03server.core.trade.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.request.UpdatePostRequest
import com.wafflestudio.team03server.core.trade.api.response.PostResponse
import com.wafflestudio.team03server.core.trade.service.TradePostService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/products")
class TradePostController(
    val tradePostService: TradePostService,
) {

    @Authenticated
    @PostMapping("")
    fun createPost(@UserContext userId: Long, @Valid @RequestBody request: CreatePostRequest): PostResponse {
        return tradePostService.createPost(userId, request)
    }

    @GetMapping("/{pid}")
    fun getPost(@PathVariable(name = "pid") postId: Long): PostResponse {
        return tradePostService.getPost(postId)
    }

    @GetMapping("")
    fun getPosts(): List<PostResponse> {
        return tradePostService.getPosts()
    }

    @Authenticated
    @PatchMapping("/{pid}")
    fun updatePost(
        @UserContext userId: Long,
        @PathVariable(name = "pid") postId: Long,
        @Valid @RequestBody request: UpdatePostRequest
    ): PostResponse {
        return tradePostService.updatePost(userId, postId, request)
    }

    @Authenticated
    @DeleteMapping("/{pid}")
    fun deletePost(@UserContext userId: Long, @PathVariable(name = "pid") postId: Long) {
        return tradePostService.removePost(userId, postId)
    }
}
