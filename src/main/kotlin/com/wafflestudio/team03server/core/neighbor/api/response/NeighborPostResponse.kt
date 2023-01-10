package com.wafflestudio.team03server.core.neighbor.api.response

import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import java.time.LocalDateTime

data class NeighborPostResponse(
    val postId: Long,
    val title: String,
    val content: String,
    val publisher: SimpleUserResponse,
    val comments: List<NeighborCommentResponse>,
    val viewCount: Int,
    val likeCount: Int,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun of(post: NeighborPost): NeighborPostResponse {
            return NeighborPostResponse(
                postId = post.id,
                title = post.title,
                content = post.content,
                publisher = SimpleUserResponse.of(post.publisher),
                comments = post.comments.map { NeighborCommentResponse.of(it) },
                viewCount = post.viewCount,
                likeCount = post.likes.size,
                createdAt = post.createdAt
            )
        }
    }
}
