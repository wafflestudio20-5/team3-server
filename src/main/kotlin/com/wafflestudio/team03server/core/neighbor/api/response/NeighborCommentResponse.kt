package com.wafflestudio.team03server.core.neighbor.api.response

import com.wafflestudio.team03server.core.neighbor.entity.NeighborComment
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import java.time.LocalDateTime

data class NeighborCommentResponse(
    val commentId: Long,
    val commenter: SimpleUserResponse,
    val comment: String,
    val replies: List<NeighborReplyResponse>,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun of(comment: NeighborComment): NeighborCommentResponse {
            return NeighborCommentResponse(
                commentId = comment.id,
                commenter = SimpleUserResponse.of(comment.commenter),
                comment = comment.comment,
                replies = comment.replies.map {NeighborReplyResponse.of(it)},
                createdAt = comment.createdAt
            )
        }
    }
}
