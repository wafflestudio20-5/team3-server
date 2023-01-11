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

    @NotNull
    val score: Double,
    var content: String? = null,

    @NotNull
    @Enumerated(EnumType.STRING)
    val reviewer: Reviewer
) : BaseTimeEntity() {
}
