package com.wafflestudio.team03server.core.trade.api.response

import com.querydsl.core.QueryResults
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.User

data class PostListResponse(
    val paging: PagingResponse,
    val posts: List<PostResponse> = mutableListOf(),
) {
    companion object {
        fun of(queryResult: QueryResults<TradePost>, user: User): PostListResponse {
            return PostListResponse(
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
) {
    companion object {
        fun of(queryResult: QueryResults<TradePost>): PagingResponse {
            return PagingResponse(
                limit = queryResult.limit,
                offset = queryResult.offset,
                total = queryResult.total,
            )
        }
    }
}
