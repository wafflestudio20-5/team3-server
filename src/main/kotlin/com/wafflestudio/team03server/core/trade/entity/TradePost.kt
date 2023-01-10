package com.wafflestudio.team03server.core.trade.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.trade.entity.TradeState.*
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.*

@Entity
class TradePost(
    var title: String,
    var description: String,
    var price: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    var seller: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    var buyer: User? = null,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    var reservations: MutableList<Reservation> = mutableListOf(),

    @OneToMany(mappedBy = "likedPost", cascade = [CascadeType.ALL])
    var likeTradePosts: MutableList<LikePost> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    var tradeState: TradeState = TRADING,
    var viewCount: Int = 0,

) : BaseTimeEntity()
