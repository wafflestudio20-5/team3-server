package com.wafflestudio.team03server.core.neighbor.api.response

import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.user.entity.User

data class NeighborPostPageResponse(
    val paging: NeighborPagingResponse,
    val posts: List<NeighborPostResponse>
) {
    companion object {
        fun of(posts: List<NeighborPost>, user: User, limit: Int, offset: Long, total: Long): NeighborPostPageResponse {
            return NeighborPostPageResponse(
                paging = NeighborPagingResponse.of(limit, offset, total),
                posts = posts.map { NeighborPostResponse.of(it, user.id) }
            )
        }
    }
}

data class NeighborPagingResponse(
    val total: Long,
    val limit: Int,
    val count: Long,
    val offset: Long,
    val hasNext: Boolean
) {
    companion object {
        fun of(limit: Int, offset: Long, total: Long): NeighborPagingResponse {
            return NeighborPagingResponse(
                total = total,
                limit = limit,
                count = countPage(offset, limit),
                offset = offset,
                hasNext = hasNext(offset, limit, total)
            )
        }

        private fun countPage(offset: Long, limit: Int) = offset / limit

        private fun hasNext(offset: Long, limit: Int, total: Long) =
            (limit + offset) < total
    }
}
