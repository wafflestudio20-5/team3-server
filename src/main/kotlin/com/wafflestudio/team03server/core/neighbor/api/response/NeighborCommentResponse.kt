package com.wafflestudio.team03server.core.neighbor.api.response

import com.wafflestudio.team03server.core.neighbor.entity.NeighborComment
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import java.time.LocalDateTime

data class NeighborCommentResponse(
    val commentId: Long,
    val commenter: SimpleUserResponse,
    val comment: String,
    val replies: List<NeighborReplyResponse>,
    val isOwner: Boolean,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
) {
    companion object {
        fun of(comment: NeighborComment, userId: Long): NeighborCommentResponse {
            return NeighborCommentResponse(
                commentId = comment.id,
                commenter = SimpleUserResponse.of(comment.commenter),
                comment = comment.comment,
                replies = comment.replies.map { NeighborReplyResponse.of(it, userId) },
                isOwner = comment.commenter.id == userId,
                createdAt = comment.createdAt,
                modifiedAt = comment.modifiedAt
            )
        }
    }
}
