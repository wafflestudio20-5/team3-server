package com.wafflestudio.team03server.core.trade.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Review(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_post_id")
    val tradePost: TradePost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    val reviewer: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    val reviewee: User,

    @NotNull
    val score: Double,
    var content: String? = null
) : BaseTimeEntity() {

    companion object {
        fun create(tradePost: TradePost, reviewer: User, reviewee: User, score: Double, content: String?): Review {
            val review = Review(
                tradePost = tradePost,
                reviewer = reviewer,
                reviewee = reviewee,
                score = score,
                content = content
            )
            reviewer.reviewsIWrote.add(review)
            reviewee.reviewsIGot.add(review)
            tradePost.reviews.add(review)
            return review
        }
    }
}
