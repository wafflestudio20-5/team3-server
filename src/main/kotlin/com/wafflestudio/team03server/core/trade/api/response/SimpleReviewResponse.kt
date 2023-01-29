package com.wafflestudio.team03server.core.trade.api.response

import com.wafflestudio.team03server.core.trade.entity.Review
import com.wafflestudio.team03server.core.trade.entity.ReviewerType
import java.time.LocalDateTime

data class SimpleReviewResponse(
    val id: Long,
    val type: ReviewerType,
    val createdAt: LocalDateTime,
    val content: String?
) {
    companion object {
        fun of(review: Review): SimpleReviewResponse {
            return SimpleReviewResponse(
                id = review.id,
                type = review.reviewerType,
                createdAt = review.createdAt!!,
                content = review.content
            )
        }
    }
}
