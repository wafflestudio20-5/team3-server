package com.wafflestudio.team03server.core.trade.api.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import com.wafflestudio.team03server.core.trade.entity.TradeStatus.*
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.entity.User
import java.time.LocalDateTime

data class PostResponse(
    val postId: Long,
    val title: String,
    val desc: String,
    val price: Int,
    val imageUrls: List<String>,
    val seller: SimpleUserResponse,
    val buyer: SimpleUserResponse? = null,
    val reservationCount: Int = 0,
    val tradeStatus: TradeStatus = TRADING,
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val isLiked: Boolean = false,
    val isOwner: Boolean = true,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val modifiedAt: LocalDateTime,
) {
    companion object {
        fun of(post: TradePost, user: User): PostResponse {
            return PostResponse(
                postId = post.id,
                title = post.title,
                desc = post.description,
                price = post.price,
                imageUrls = getImgUrls(post),
                seller = SimpleUserResponse.of(post.seller),
                buyer = post.buyer?.let { SimpleUserResponse.of(it) },
                reservationCount = post.reservations.size,
                tradeStatus = post.tradeStatus,
                viewCount = post.viewCount,
                likeCount = post.likeTradePosts.size,
                isLiked = isLiked(user, post),
                isOwner = isOwner(user, post),
                createdAt = post.createdAt!!,
                modifiedAt = post.modifiedAt!!,
            )
        }

        private fun getImgUrls(post: TradePost) = post.images.map { it.imgUrl }

        private fun isOwner(user: User, post: TradePost): Boolean {
            return user.id == post.seller.id
        }

        private fun isLiked(user: User, post: TradePost): Boolean {
            return user.likeTradePosts.any { it.likedPost.id == post.id }
        }
    }
}
