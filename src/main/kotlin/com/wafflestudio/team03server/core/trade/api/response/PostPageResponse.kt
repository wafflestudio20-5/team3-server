package com.wafflestudio.team03server.core.trade.api.response

import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.domain.Pageable

data class PostPageResponse(
    val paging: PagingResponse,
    val posts: List<PostResponse>
) {
    companion object {
        fun of(pageable: Pageable, posts: List<TradePost>, user: User, total: Long): PostPageResponse {
            return PostPageResponse(
                paging = PagingResponse.of(pageable, posts, total),
                posts = posts.map { PostResponse.of(it, user) }
            )
        }
    }
}

data class PagingResponse(
    val limit: Int,
    val offset: Long,
    val total: Long,
    val count: Long,
    val hasNext: Boolean,
) {
    companion object {
        fun of(pageable: Pageable, posts: List<TradePost>, total: Long): PagingResponse {
            return PagingResponse(
                limit = pageable.pageSize,
                offset = pageable.offset,
                total = total,
                count = calcPage(pageable),
                hasNext = hasNext(pageable, total)
            )
        }

        private fun calcPage(pageable: Pageable) =
            pageable.offset / pageable.pageSize

        private fun hasNext(pageable: Pageable, total: Long) =
            (pageable.pageSize + pageable.offset) < total
    }
}
