package com.wafflestudio.team03server.core.trade.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.trade.entity.TradeStatus.*
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
    var images: MutableList<TradePostImage> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    var reservations: MutableList<Reservation> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val chatRooms: MutableList<ChatRoom> = mutableListOf(),

    @OneToMany(mappedBy = "likedPost", cascade = [CascadeType.ALL])
    var likeTradePosts: MutableList<LikePost> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    var tradeStatus: TradeStatus = TRADING,
    var viewCount: Int = 0,

    @OneToMany(mappedBy = "tradePost")
    var reviews: MutableList<Review> = mutableListOf()

) : BaseTimeEntity()
