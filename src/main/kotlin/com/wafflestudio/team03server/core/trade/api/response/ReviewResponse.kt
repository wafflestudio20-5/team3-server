package com.wafflestudio.team03server.core.trade.api.response

import com.wafflestudio.team03server.core.trade.entity.Review
import com.wafflestudio.team03server.core.trade.entity.ReviewerType
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val user: SimpleUserResponse,
    val type: ReviewerType,
    val createdAt: LocalDateTime,
    val content: String
) {
    companion object {
        fun of(review: Review): ReviewResponse {
            return ReviewResponse(
                id = review.id,
                user = SimpleUserResponse.of(review.reviewer),
                type = review.reviewerType,
                createdAt = review.createdAt!!,
                content = review.content!!
            )
        }
    }
}
