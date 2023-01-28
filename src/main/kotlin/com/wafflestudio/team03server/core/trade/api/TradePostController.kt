package com.wafflestudio.team03server.core.trade.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.request.UpdatePostRequest
import com.wafflestudio.team03server.core.trade.api.response.PostListResponse
import com.wafflestudio.team03server.core.trade.api.response.PostPageResonse
import com.wafflestudio.team03server.core.trade.api.response.PostResponse
import com.wafflestudio.team03server.core.trade.api.response.ReservationResponse
import com.wafflestudio.team03server.core.trade.service.TradePostService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/tradepost")
class TradePostController(
    val tradePostService: TradePostService,
) {

    @Authenticated
    @PostMapping("")
    fun createPost(
        @UserContext userId: Long,
        @Valid @RequestBody request: CreatePostRequest
    ): PostResponse {
        return tradePostService.createPost(userId, request)
    }

    @Authenticated
    @GetMapping("/{post-id}")
    fun getPost(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long): PostResponse {
        return tradePostService.getPost(userId, postId)
    }

    @Authenticated
    @GetMapping("")
    fun getAllPosts(
        @UserContext userId: Long,
        @RequestParam("keyword") keyword: String?,
        @RequestParam("page", required = false, defaultValue = "1") page: Int?,
        @RequestParam("limit", required = false, defaultValue = "10") limit: Int?
    ): PostPageResonse {
        val pageable = PageRequest.of(page!! - 1, limit!!)
        return tradePostService.getAllPosts(userId, keyword, pageable)
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
    ): ReservationResponse {
        return tradePostService.changeBuyer(userId, buyerId, postId)
    }

    @Authenticated
    @PostMapping("/{post-id}/cancel")
    fun cancelTrade(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long) {
        tradePostService.cancelTrade(userId, postId)
    }

    // 구매 확정
    @Authenticated
    @PostMapping("/{post-id}/confirmation")
    fun confirmTrade(
        @UserContext userId: Long,
        @PathVariable(name = "post-id") postId: Long
    ): ReservationResponse {
        return tradePostService.confirmTrade(userId, postId)
    }

    // 찜처리
    @Authenticated
    @PostMapping("/{post-id}/like")
    fun likeTradePost(@UserContext userId: Long, @PathVariable(name = "post-id") postId: Long) {
        tradePostService.likePost(userId, postId)
    }

    // 탑3 포스트
    @Authenticated
    @GetMapping("/top3")
    fun getTopThreePosts(@UserContext userId: Long): PostListResponse {
        return tradePostService.getTopThreePosts(userId)
    }
}
