package com.wafflestudio.team03server.core.trade.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.trade.entity.QReview.review
import com.wafflestudio.team03server.core.trade.entity.Review
import org.springframework.stereotype.Repository

@Repository
class ReviewCustomRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findByRevieweeIdAndContentIsNotNull(revieweeId: Long): List<Review> {
        return queryFactory
            .selectFrom(review).distinct()
            .innerJoin(review.reviewer).fetchJoin()
            .where(review.reviewee.id.eq(revieweeId))
            .where(review.content.isNotEmpty)
            .orderBy(review.createdAt.desc())
            .fetch()
    }
}
