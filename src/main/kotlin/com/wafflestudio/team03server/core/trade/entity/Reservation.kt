package com.wafflestudio.team03server.core.trade.entity

import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.*

@Entity
class Reservation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: TradePost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    var buyer: User,
) {

    companion object {
        fun create(_buyer: User, _post: TradePost): Reservation {
            val reservation = Reservation(post = _post, buyer = _buyer)
            _post.reservations.add(reservation)
            return reservation
        }
    }
}
