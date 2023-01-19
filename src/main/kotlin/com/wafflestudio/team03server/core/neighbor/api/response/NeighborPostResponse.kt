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
    val commentCount: Int,
    val viewCount: Int,
    val likeCount: Int,
    val isOwner: Boolean,
    val isLiked: Boolean? = false,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
) {
    companion object {
        fun of(post: NeighborPost, userId: Long): NeighborPostResponse {
            return NeighborPostResponse(
                postId = post.id,
                title = post.title,
                content = post.content,
                publisher = SimpleUserResponse.of(post.publisher),
                comments = post.comments.map { NeighborCommentResponse.of(it, userId) },
                commentCount = post.comments.size,
                viewCount = post.viewCount,
                likeCount = post.likes.filter { !it.deleteStatus }.size,
                isOwner = post.publisher.id == userId,
                isLiked = post.likes.any { !it.deleteStatus && it.liker.id == userId },
                createdAt = post.createdAt,
                modifiedAt = post.modifiedAt
            )
        }
    }
}
