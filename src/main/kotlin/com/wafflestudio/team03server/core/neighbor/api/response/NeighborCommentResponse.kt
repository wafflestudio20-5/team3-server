package com.wafflestudio.team03server.core.neighbor.api.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.wafflestudio.team03server.core.neighbor.entity.NeighborComment
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import java.time.LocalDateTime

data class NeighborCommentResponse(
    val commentId: Long,
    val commenter: SimpleUserResponse,
    val comment: String,
    val replies: List<NeighborReplyResponse>,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime?
) {
    companion object {
        fun of(comment: NeighborComment): NeighborCommentResponse {
            return NeighborCommentResponse(
                commentId = comment.id,
                commenter = SimpleUserResponse.of(comment.commenter),
                comment = comment.comment,
                replies = comment.replies.map { NeighborReplyResponse.of(it) },
                createdAt = comment.createdAt
            )
        }
    }
}
