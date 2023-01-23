package com.wafflestudio.team03server.core.chat.repository

import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository {
    fun findChatRoomByBuyerAndSellerAndPost(buyer: User, seller: User, post: TradePost): ChatRoom?
    fun findChatRoomByRoomUUID(roomUUID: String): ChatRoom?
}
