package com.wafflestudio.team03server.core.chat.service

import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.chat.api.response.RoomUUIDResponse
import com.wafflestudio.team03server.core.chat.api.dto.ChatMessage
import com.wafflestudio.team03server.core.chat.api.response.MessagesResponse
import com.wafflestudio.team03server.core.chat.entity.ChatHistory
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.chat.repository.ChatHistoryRepository
import com.wafflestudio.team03server.core.chat.repository.ChatRoomRepository
import com.wafflestudio.team03server.core.trade.entity.Reservation
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.repository.ReservationRepository
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ChatService(
    val chatRoomRepository: ChatRoomRepository,
    val chatHistoryRepository: ChatHistoryRepository,
    val userRepository: UserRepository,
    val tradePostRepository: TradePostRepository,
    val reservationRepository: ReservationRepository,
) {
    fun startChat(buyerId: Long, postId: Long): RoomUUIDResponse {
        val findPost = getPostById(postId)
        val findBuyer = getUserById(buyerId)
        val findSeller = getUserById(findPost.seller.id)
        checkSellerStartChat(findBuyer, findSeller)

        var chatRoom: ChatRoom? = findChatRoom(findBuyer, findSeller, findPost)
        if (isFirstChatting(chatRoom)) {
            chatRoom = ChatRoom.create(findSeller, findBuyer, findPost)
            chatRoomRepository.save(chatRoom)
            makeReservation(findPost, findBuyer) // 채팅방이 생기면 예약 시작
        }

        return RoomUUIDResponse(chatRoom!!.roomUUID)
    }

    private fun makeReservation(findPost: TradePost, findBuyer: User) {
        val reservation = Reservation.create(findBuyer, findPost)
        reservationRepository.save(reservation)
    }

    private fun checkSellerStartChat(buyer: User, seller: User) {
        if (buyer == seller) throw Exception400("판매자는 채팅을 시작할 수 없습니다.")
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
        val chatRoom = getChatRoomByUUID(message.roomUUID)
        val sender = getUserById(message.senderId)
        val _message = message.message
        val createdAt = message.createdAt
        saveChatMessage(chatRoom, sender, _message, createdAt)
    }

    private fun saveChatMessage(chatRoom: ChatRoom, sender: User, message: String, createdAt: LocalDateTime) {
        val chatHistory = ChatHistory.create(
            _chatRoom = chatRoom,
            _sender = sender,
            _message = message,
            _createdAt = createdAt,
        )
        val savedChatHistory = chatHistoryRepository.save(chatHistory)
    }

    private fun getChatRoomByUUID(roomUUID: String) =
        chatRoomRepository.findChatRoomByRoomUUID(roomUUID) ?: throw Exception404("잘못된 채팅방 UUID입니다.")

    fun getMessages(userId: Long, roomUUID: String, youId: Long): MessagesResponse {
        val you = getUserById(youId)
        val chatRoom = getChatRoomByUUID(roomUUID)
        return MessagesResponse.of(you, chatRoom)
    }
}
