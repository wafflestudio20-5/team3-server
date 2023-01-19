package com.wafflestudio.team03server.core.trade.api.response

import com.querydsl.core.QueryResults
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.User

data class PostPageResonse(
    val paging: PagingResponse,
    val posts: List<PostResponse> = mutableListOf(),
) {
    companion object {
        fun of(queryResult: QueryResults<TradePost>, user: User): PostPageResonse {
            return PostPageResonse(
                paging = PagingResponse.of(queryResult),
                posts = getPosts(queryResult.results, user)
            )
        }

        private fun getPosts(results: List<TradePost>, user: User): List<PostResponse> {
            return results.map { PostResponse.of(it, user) }
        }
    }
}

data class PagingResponse(
    val limit: Long,
    val offset: Long,
    val total: Long,
    val count: Long,
    val hasNext: Boolean,
) {
    companion object {
        fun of(queryResult: QueryResults<TradePost>): PagingResponse {
            return PagingResponse(
                limit = queryResult.limit,
                offset = queryResult.offset,
                total = queryResult.total,
                count = calcPage(queryResult),
                hasNext = hasNext(queryResult)
            )
        }

        private fun calcPage(queryResult: QueryResults<TradePost>) =
            queryResult.offset / queryResult.limit

        private fun hasNext(queryResult: QueryResults<TradePost>) =
            (queryResult.limit + queryResult.offset) < queryResult.total
    }
}
