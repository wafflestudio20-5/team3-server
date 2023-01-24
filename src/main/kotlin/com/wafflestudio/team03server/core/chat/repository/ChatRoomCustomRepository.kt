package com.wafflestudio.team03server.core.chat.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.chat.entity.QChatRoom.chatRoom

interface ChatRoomCustomRepository {
    fun findChatRoomsWithSellerAndBuyerAndPostBySellerId(sellerId: Long): List<ChatRoom>
}

class ChatRoomCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : ChatRoomCustomRepository {
    override fun findChatRoomsWithSellerAndBuyerAndPostBySellerId(sellerId: Long): List<ChatRoom> {
        return jpaQueryFactory
            .selectFrom(chatRoom)
            .innerJoin(chatRoom.buyer).fetchJoin()
            .innerJoin(chatRoom.seller).fetchJoin()
            .innerJoin(chatRoom.post).fetchJoin()
            .where(chatRoom.seller.id.eq(sellerId))
            .fetch()
    }
}
