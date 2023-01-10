package com.wafflestudio.team03server.core.trade.api.response

import com.wafflestudio.team03server.core.trade.entity.Reservation
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeState
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse

data class ReservationResponse(
    val tradeState: TradeState,
    val buyer: SimpleUserResponse? = null,
    val candidates: List<SimpleUserResponse>,
) {
    companion object {
        fun of(post: TradePost): ReservationResponse {
            return ReservationResponse(
                tradeState = post.tradeState,
                buyer = post.buyer?.let { SimpleUserResponse.of(post.buyer!!) },
                candidates = getCandidates(post.reservations)
            )
        }

        private fun getCandidates(reservations: MutableList<Reservation>): List<SimpleUserResponse> {
            return reservations.map { SimpleUserResponse.of(it.buyer) }
        }
    }
}
