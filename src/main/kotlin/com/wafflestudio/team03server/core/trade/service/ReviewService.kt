package com.wafflestudio.team03server.core.trade.service

import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.trade.api.request.CreateReviewRequest
import com.wafflestudio.team03server.core.trade.api.response.ReviewResponse
import com.wafflestudio.team03server.core.trade.entity.Review
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import com.wafflestudio.team03server.core.trade.repository.ReviewRepository
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.round

@Service
@Transactional
class ReviewService(
    private val tradePostRepository: TradePostRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {
    fun createReview(reviewerId: Long, postId: Long, createReviewRequest: CreateReviewRequest) {
        val post = getPostById(postId)
        val reviewer = getUserById(reviewerId)
        checkTradeFinished(post)
        checkReviewerBuyerOrSeller(reviewer, post)
        val reviewee = if (reviewer == post.seller) post.buyer else post.seller
        val (score, content) = createReviewRequest
        val review = Review.create(post, reviewer, reviewee!!, score, content)
        reviewee.temperature = round((reviewee.temperature + score) * 10) / 10
        reviewRepository.save(review)
    }

    fun getReviews(userId: Long): List<ReviewResponse> {
        getUserById(userId)
        val reviewEntities = reviewRepository.findByRevieweeIdAndContentIsNotNull(userId)
        val reviews = mutableListOf<ReviewResponse>()
        for (reviewEntity in reviewEntities) {
            reviews.add(ReviewResponse.of(reviewEntity))
        }
        return reviews
    }

    fun deleteReview(userId: Long, reviewId: Long) {
        val review = getReviewById(reviewId)
        val reviewer = getUserById(userId)
        checkReviewer(reviewer, review)
        reviewRepository.delete(review)
    }

    private fun getPostById(postId: Long): TradePost {
        return tradePostRepository.findByIdOrNull(postId) ?: throw Exception404("글을 찾을 수 없습니다.")
    }

    private fun getUserById(userId: Long): User {
        return userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
    }

    private fun getReviewById(reviewId: Long): Review {
        return reviewRepository.findByIdOrNull(reviewId) ?: throw Exception404("후기를 찾을 수 없습니다.")
    }

    private fun checkTradeFinished(post: TradePost) {
        if (post.tradeStatus != TradeStatus.COMPLETED) {
            throw Exception403("거래 완료 상태에서만 후기를 작성할 수 있습니다.")
        }
    }

    private fun checkReviewerBuyerOrSeller(reviewer: User, post: TradePost) {
        if (reviewer != post.buyer && reviewer != post.seller) {
            throw Exception403("거래 당사자만 후기를 작성할 수 있습니다.")
        }
    }

    private fun checkReviewer(reviewer: User, review: Review) {
        if (review.reviewer != reviewer) {
            throw Exception403("내가 쓴 후기만 삭제할 수 있습니다.")
        }
    }
}
