package com.wafflestudio.team03server.core.trade.api.response

import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeState
import com.wafflestudio.team03server.core.trade.entity.TradeState.*
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse

data class PostResponse(
    val postId: Long,
    val title: String,
    val desc: String,
    val price: Int,
    val seller: SimpleUserResponse,
    val buyer: SimpleUserResponse? = null,
    val reservationCount: Int = 0,
    val tradeStatus: TradeState = TRADING,
    val viewCount: Int = 0,
) {
    companion object {
        fun of(post: TradePost): PostResponse {
            return PostResponse(
                postId = post.id,
                title = post.title,
                desc = post.description,
                price = post.price,
                seller = SimpleUserResponse.of(post.seller),
                buyer = post.buyer?.let { SimpleUserResponse.of(it) },
                reservationCount = post.reservations.size, // 추후 N + 1 문제 고려해서 리팩토링
                tradeStatus = post.tradeState,
                viewCount = post.viewCount,
            )
        }
    }
}
