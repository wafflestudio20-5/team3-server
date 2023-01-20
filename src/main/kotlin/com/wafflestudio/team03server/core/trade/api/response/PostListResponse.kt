package com.wafflestudio.team03server.core.trade.api.response

import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.User

data class PostListResponse(
    val posts: List<PostResponse>,
) {
    companion object {
        fun of(user: User, posts: List<TradePost>): PostListResponse {
            return PostListResponse(
                posts = posts.map { PostResponse.of(it, user) }
            )
        }
    }
}
