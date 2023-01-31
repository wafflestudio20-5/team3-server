package com.wafflestudio.team03server.core.neighbor.api.response

import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.user.entity.User

data class NeighborPostListResponse(
    val posts: List<NeighborPostResponse>
) {
    companion object {
        fun of(posts: List<NeighborPost>, user: User): NeighborPostListResponse {
            return NeighborPostListResponse(posts = posts.map { NeighborPostResponse.of(it, user.id) })
        }
    }
}
