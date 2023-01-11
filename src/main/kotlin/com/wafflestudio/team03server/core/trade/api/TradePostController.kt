package com.wafflestudio.team03server.core.trade.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.request.UpdatePostRequest
import com.wafflestudio.team03server.core.trade.api.response.PostResponse
import com.wafflestudio.team03server.core.trade.api.response.ReservationResponse
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

    @Authenticated
    @GetMapping("/{post-id}")
    fun getPost(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long): PostResponse {
        return tradePostService.getPost(userId, postId)
    }

    // TODO: 스펙 확인해서 추후 수정하기
    @Authenticated
    @GetMapping("")
    fun getPosts(@UserContext userId: Long): List<PostResponse> {
        return tradePostService.getPosts(userId)
    }

    @Authenticated
    @PatchMapping("/{post-id}")
    fun updatePost(
        @UserContext userId: Long,
        @PathVariable(name = "post-id") postId: Long,
        @Valid @RequestBody request: UpdatePostRequest
    ): PostResponse {
        return tradePostService.updatePost(userId, postId, request)
    }

    @Authenticated
    @DeleteMapping("/{post-id}")
    fun deletePost(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long) {
        return tradePostService.removePost(userId, postId)
    }

    @Authenticated
    @GetMapping("/{post-id}/reservation")
    fun getReservations(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long): ReservationResponse {
        return tradePostService.getReservations(userId, postId)
    }

    @Authenticated
    @PostMapping("/{post-id}/reservation/{uid}")
    fun changeBuyer(
        @UserContext userId: Long,
        @PathVariable(name = "post-id") postId: Long,
        @PathVariable(name = "uid") buyerId: Long,
    ) {
        tradePostService.changeBuyer(userId, buyerId, postId)
    }

    @Authenticated
    @PostMapping("/{post-id}/cancel")
    fun cancelTrade(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long) {
        tradePostService.cancelTrade(userId, postId)
    }

    // 구매 확정
    @Authenticated
    @PostMapping("/{post-id}/confirmation")
    fun confirmTrade(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long) {
        tradePostService.confirmTrade(userId, postId)
    }

    // 찜처리
    @Authenticated
    @PostMapping("/{post-id}/like")
    fun likeTradePost(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long) {
        tradePostService.likePost(userId, postId)
    }
}
