package com.wafflestudio.team03server.core.chat.service

import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.chat.api.response.ChatResponse
import com.wafflestudio.team03server.core.chat.dto.ChatMessage
import com.wafflestudio.team03server.core.chat.entity.ChatHistory
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.chat.repository.ChatHistoryRepository
import com.wafflestudio.team03server.core.chat.repository.ChatRoomRepository
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatService(
    val chatRoomRepository: ChatRoomRepository,
    val chatHistoryRepository: ChatHistoryRepository,
    val userRepository: UserRepository,
    val tradePostRepository: TradePostRepository,
) {
    fun startChat(buyerId: Long, postId: Long): ChatResponse {
        val findPost = getPostById(postId)
        val findBuyer = getUserById(buyerId)
        val findSeller = getUserById(findPost.seller.id)

        var chatRoom: ChatRoom? = findChatRoom(findBuyer, findSeller, findPost)
        if (isFirstChatting(chatRoom)) {
            chatRoom = ChatRoom.create(findSeller, findBuyer, findPost)
            chatRoomRepository.save(chatRoom)
            // TODO: 예약기능까지 연동하기
        }

        return ChatResponse.of(chatRoom!!) // TODO: 추후 N + 1 해결하기
    }

    private fun isFirstChatting(chatRoom: ChatRoom?) = (chatRoom == null)

    private fun findChatRoom(findBuyer: User, findSeller: User, findPost: TradePost): ChatRoom? {
        return chatRoomRepository.findChatRoomByBuyerAndSellerAndPost(findBuyer, findSeller, findPost)
    }

    private fun getPostById(postId: Long): TradePost =
        tradePostRepository.findByIdOrNull(postId) ?: throw Exception404("id: ${postId}에 해당하는 글이 존재하지 않습니다.")

    private fun getUserById(userId: Long): User =
        userRepository.findByIdOrNull(userId) ?: throw Exception404("id: ${userId}에 해당하는 유저가 존재하지 않습니다.")

    fun saveMessage(message: ChatMessage) {
        val _chatRoom = getChatRoomByUUID(message)
        val _sender = getUserById(message.senderId)
        val _message = message.message
        saveChatMessage(_chatRoom, _sender, _message)
    }

    private fun saveChatMessage(_chatRoom: ChatRoom, _sender: User, _message: String) {
        val chatHistory = ChatHistory(
            chatRoom = _chatRoom,
            sender = _sender,
            message = _message,
        )
        chatHistoryRepository.save(chatHistory)
    }

    private fun getChatRoomByUUID(message: ChatMessage) =
        chatRoomRepository.findChatRoomByRoomUUID(message.roomUUID) ?: throw Exception404("잘못된 채팅방 UUID입니다.")
}
