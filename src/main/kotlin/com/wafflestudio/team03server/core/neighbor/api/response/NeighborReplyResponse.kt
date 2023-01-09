package com.wafflestudio.team03server.core.neighbor.api.response

import com.wafflestudio.team03server.core.neighbor.entity.NeighborReply
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import java.time.LocalDateTime

data class NeighborReplyResponse(
    val replyId: Long,
    val replyer: SimpleUserResponse,
    val replyingMessage: String,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun of(reply: NeighborReply): NeighborReplyResponse {
            return NeighborReplyResponse(
                replyId = reply.id,
                replyer = SimpleUserResponse.of(reply.replyer),
                replyingMessage = reply.replyingMessage,
                createdAt = reply.createdAt
            )
        }
    }
}
