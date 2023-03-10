package com.wafflestudio.team03server.core.trade.service

import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.trade.api.request.CreateReviewRequest
import com.wafflestudio.team03server.core.trade.api.response.ReviewResponse
import com.wafflestudio.team03server.core.trade.entity.Review
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import com.wafflestudio.team03server.core.trade.repository.ReviewCustomRepository
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
    private val reviewRepository: ReviewRepository,
    private val reviewCustomRepository: ReviewCustomRepository
) {
    fun createReview(reviewerId: Long, postId: Long, createReviewRequest: CreateReviewRequest) {
        val post = getPostById(postId)
        val reviewer = getUserById(reviewerId)
        checkTradeFinished(post)
        checkReviewerBuyerOrSeller(reviewer, post)
        checkDuplicateReview(postId, reviewerId)
        val reviewee = if (reviewer == post.seller) post.buyer else post.seller
        val (score, content) = createReviewRequest
        val review = Review.create(post, reviewer, reviewee!!, score!!, content)
        reviewee.temperature = round((reviewee.temperature + score) * 10) / 10
        if (reviewee.temperature < 0) {
            reviewee.temperature = 0.0
        } else if (reviewee.temperature > 100) {
            reviewee.temperature = 100.0
        }
        reviewRepository.save(review)
    }

    fun getReviews(userId: Long): List<ReviewResponse> {
        getUserById(userId)
        val reviewEntities = reviewCustomRepository.findByRevieweeIdAndContentIsNotNull(userId)
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
        review.reviewee.temperature = round((review.reviewee.temperature - review.score) * 10) / 10
        reviewRepository.delete(review)
    }

    private fun getPostById(postId: Long): TradePost {
        return tradePostRepository.findByIdOrNull(postId) ?: throw Exception404("?????? ?????? ??? ????????????.")
    }

    private fun getUserById(userId: Long): User {
        return userRepository.findByIdOrNull(userId) ?: throw Exception404("???????????? ?????? ??? ????????????.")
    }

    private fun getReviewById(reviewId: Long): Review {
        return reviewRepository.findByIdOrNull(reviewId) ?: throw Exception404("????????? ?????? ??? ????????????.")
    }

    private fun checkTradeFinished(post: TradePost) {
        if (post.tradeStatus != TradeStatus.COMPLETED) {
            throw Exception403("?????? ?????? ??????????????? ????????? ????????? ??? ????????????.")
        }
    }

    private fun checkReviewerBuyerOrSeller(reviewer: User, post: TradePost) {
        if (reviewer != post.buyer && reviewer != post.seller) {
            throw Exception403("?????? ???????????? ????????? ????????? ??? ????????????.")
        }
    }

    private fun checkDuplicateReview(tradePostId: Long, reviewerId: Long) {
        if (reviewRepository.findByTradePostIdAndReviewerId(tradePostId, reviewerId) != null) {
            throw Exception403("?????? ????????? ????????? ????????????.")
        }
    }

    private fun checkReviewer(reviewer: User, review: Review) {
        if (review.reviewer != reviewer) {
            throw Exception403("?????? ??? ????????? ????????? ??? ????????????.")
        }
    }
}
