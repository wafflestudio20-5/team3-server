package com.wafflestudio.team03server.core.chat.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.chat.entity.QChatRoom.chatRoom

interface ChatRoomCustomRepository {
    fun findChatRoomsWithAllByUserId(userId: Long): List<ChatRoom>
}

class ChatRoomCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : ChatRoomCustomRepository {
    override fun findChatRoomsWithAllByUserId(userId: Long): List<ChatRoom> {
        return jpaQueryFactory
            .select(chatRoom).distinct()
            .from(chatRoom)
            .innerJoin(chatRoom.buyer).fetchJoin()
            .innerJoin(chatRoom.seller).fetchJoin()
            .innerJoin(chatRoom.post).fetchJoin()
            .leftJoin(chatRoom.histories).fetchJoin()
            .where(chatRoom.seller.id.eq(userId).or(chatRoom.buyer.id.eq(userId)))
            .fetch()
    }
}
