package com.wafflestudio.team03server.core.chat.service

import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.core.chat.api.dto.ReceivedChatMessage
import com.wafflestudio.team03server.core.chat.repository.ChatHistoryRepository
import com.wafflestudio.team03server.core.chat.repository.ChatRoomRepository
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.service.TradePostService
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
internal class ChatServiceTest @Autowired constructor(
    val userRepository: UserRepository,
    val tradePostService: TradePostService,
    val chatRoomRepository: ChatRoomRepository,
    val chatHistoryRepository: ChatHistoryRepository,
    val chatService: ChatService,
) {

    @Test
    fun 채팅_시작_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000, mutableListOf("img1", "img2"))
        val post1 = tradePostService.createPost(savedUser1.id, request)

        // when
        val chat = chatService.startChat(savedUser2.id, post1.postId)

        // then
        assertThat(chat.roomUUID.length).isGreaterThan(10)
        assertThat(tradePostService.getPost(savedUser2.id, post1.postId).reservationCount).isEqualTo(1)
    }

    @Test
    fun 채팅_시작_실패_판매자가_채팅시작() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000, mutableListOf("img1", "img2"))
        val post1 = tradePostService.createPost(savedUser1.id, request)

        // when
        org.junit.jupiter.api.assertThrows<Exception400> { chatService.startChat(savedUser1.id, post1.postId) }
    }

    @Test
    fun 채팅_저장_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000, mutableListOf("img1", "img2"))
        val post1 = tradePostService.createPost(savedUser1.id, request)
        val chat = chatService.startChat(savedUser2.id, post1.postId)
        val chatMessage = ReceivedChatMessage(chat.roomUUID, savedUser2.id, "안녕하세요!", LocalDateTime.now())

        // when
        chatService.saveMessage(chatMessage)
        val history = chatHistoryRepository.findChatHistoryBySender(savedUser2)!!

        // then
        assertThat(history.chatRoom.roomUUID).isEqualTo(chat.roomUUID)
        assertThat(history.chatRoom.seller).isEqualTo(savedUser1)
        assertThat(history.chatRoom.buyer).isEqualTo(savedUser2)
        assertThat(history.chatRoom.post.title).isEqualTo("title1")
        assertThat(history.message).isEqualTo("안녕하세요!")
        assertThat(history.sender).isEqualTo(savedUser2)
        assertThat(history.readCount).isEqualTo(2)
    }

    @Test
    fun 메시지_가져오기_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000, mutableListOf("img1", "img2"))
        val post1 = tradePostService.createPost(savedUser1.id, request)
        val chat = chatService.startChat(savedUser2.id, post1.postId)
        val chatMessage = ReceivedChatMessage(chat.roomUUID, savedUser2.id, "안녕하세요!", LocalDateTime.now())
        val chatMessage2 = ReceivedChatMessage(chat.roomUUID, savedUser1.id, "안녕하세요?", LocalDateTime.now())
        chatService.saveMessage(chatMessage)
        chatService.saveMessage(chatMessage2)

        // when
        val messages = chatService.getMessages(savedUser1.id, chat.roomUUID, savedUser2.id)

        // then
        assertThat(messages.you.id).isEqualTo(savedUser2.id)
        assertThat(messages.you.imgUrl).isNull()
        assertThat(messages.you.username).isEqualTo(savedUser2.username)
        assertThat(messages.chatHistories.size).isEqualTo(2)
        assertThat(messages.chatHistories[0].message).isEqualTo("안녕하세요!")
        assertThat(messages.chatHistories[0].senderId).isEqualTo(savedUser2.id)
    }

    private fun createUser(username: String, email: String, password: String, location: String): User {
        return User(username, email, password, location, WKTReader().read("POINT(1.0 1.0)") as Point)
    }
}
