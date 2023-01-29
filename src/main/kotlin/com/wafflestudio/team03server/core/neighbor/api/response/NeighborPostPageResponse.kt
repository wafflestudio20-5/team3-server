package com.wafflestudio.team03server.core.neighbor.api.response

import com.querydsl.core.QueryResults
import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.user.entity.User

data class NeighborPostPageResponse(
    val paging: NeighborPagingResponse,
    val posts: List<NeighborPostResponse>
) {
    companion object {
        fun of(queryResults: QueryResults<NeighborPost>, user: User): NeighborPostPageResponse {
            val posts = queryResults.results
            val limit = queryResults.limit
            val offset = queryResults.offset
            val total = queryResults.total
            return NeighborPostPageResponse(
                paging = NeighborPagingResponse.of(posts, limit, offset, total),
                posts = posts.map { NeighborPostResponse.of(it, user.id) }
            )
        }
    }
}

data class NeighborPagingResponse(
    val total: Long,
    val count: Long,
    val hasNext: Boolean
) {
    companion object {
        fun of(posts: List<NeighborPost>, limit: Long, offset: Long, total: Long): NeighborPagingResponse {
            return NeighborPagingResponse(
                total = total,
                count = countPage(offset, limit),
                hasNext = hasNext(offset, limit, total)
            )
        }

        private fun countPage(offset: Long, limit: Long) = offset / limit + 1

        private fun hasNext(offset: Long, limit: Long, total: Long) =
            (limit + offset) < total
    }
}
