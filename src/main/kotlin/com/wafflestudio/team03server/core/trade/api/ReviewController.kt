package com.wafflestudio.team03server.core.trade.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.trade.api.request.CreateReviewRequest
import com.wafflestudio.team03server.core.trade.api.response.ReviewResponse
import com.wafflestudio.team03server.core.trade.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ReviewController(
    private val reviewService: ReviewService
) {

    @Authenticated
    @PostMapping("/tradepost/{post-id}/review")
    fun createReview(
        @UserContext userId: Long,
        @PathVariable("post-id") postId: Long,
        @RequestBody createReviewRequest: CreateReviewRequest
    ): ResponseEntity<Any> {
        reviewService.createReview(userId, postId, createReviewRequest)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/users/{user-id}/reviews")
    fun getReviews(@PathVariable("user-id") userId: Long): ResponseEntity<List<ReviewResponse>> {
        val reviews = reviewService.getReviews(userId)
        return ResponseEntity(reviews, HttpStatus.OK)
    }

    @Authenticated
    @DeleteMapping("/reviews/{review-id}")
    fun deleteReview(@UserContext userId: Long, @PathVariable("review-id") reviewId: Long): ResponseEntity<Any> {
        reviewService.deleteReview(userId, reviewId)
        return ResponseEntity(HttpStatus.OK)
    }
}
