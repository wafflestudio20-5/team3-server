package com.wafflestudio.team03server.core.trade.entity

import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.*

@Entity
class Reservation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: TradePost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
)
