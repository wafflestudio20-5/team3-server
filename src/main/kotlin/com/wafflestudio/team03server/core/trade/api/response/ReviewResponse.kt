package com.wafflestudio.team03server.core.trade.api.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.wafflestudio.team03server.core.trade.entity.Review
import java.time.LocalDateTime

data class ReviewResponse(
    val username: String,
    val location: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    val content: String?
) {
    companion object {
        fun of(review: Review): ReviewResponse {
            return ReviewResponse(
                review.reviewer.username,
                review.reviewer.location,
                review.createdAt!!,
                review.content
            )
        }
    }
}
