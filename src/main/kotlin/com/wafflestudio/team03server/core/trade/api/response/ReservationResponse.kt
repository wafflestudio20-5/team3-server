package com.wafflestudio.team03server.core.trade.api.response

import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import com.wafflestudio.team03server.core.user.entity.User

data class ReservationResponse(
    val tradeStatus: TradeStatus,
    val buyer: SimpleUserResponseWithRoomUUID? = null,
    val candidates: List<SimpleUserResponseWithRoomUUID>,
) {
    companion object {
        fun of(post: TradePost): ReservationResponse {
            return ReservationResponse(
                tradeStatus = post.tradeStatus,
                buyer = post.buyer?.let { SimpleUserResponseWithRoomUUID.of(post.chatRooms, post.buyer!!) },
                candidates = getCandidates(post)
            )
        }

        private fun getCandidates(post: TradePost): List<SimpleUserResponseWithRoomUUID> {
            return post.chatRooms.map { SimpleUserResponseWithRoomUUID.of(post.chatRooms, it.buyer) }
        }
    }
}

data class SimpleUserResponseWithRoomUUID(
    val id: Long,
    val email: String,
    val username: String,
    val imgUrl: String?,
    val location: String,
    val temperature: Double,
    val roomUUID: String,
) {
    companion object {
        fun of(chatRooms: MutableList<ChatRoom>, user: User): SimpleUserResponseWithRoomUUID {
            return SimpleUserResponseWithRoomUUID(
                id = user.id,
                email = user.email,
                username = user.username,
                imgUrl = user.imgUrl,
                location = user.location,
                temperature = user.temperature,
                roomUUID = chatRooms.first { it.buyer == user }.roomUUID
            )
        }
    }
}
