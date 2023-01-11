package com.wafflestudio.team03server.core.trade.entity

import javax.persistence.*

@Entity
class TradePostImage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_post_id")
    val post: TradePost,

    val imgUrl: String,
) {
    fun addImage(post: TradePost) {
        post.images.add(this)
    }
}
