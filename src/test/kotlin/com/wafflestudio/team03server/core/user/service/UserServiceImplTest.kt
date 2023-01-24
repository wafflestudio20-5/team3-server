package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.core.chat.api.dto.ChatMessage
import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.common.Exception409
import com.wafflestudio.team03server.core.chat.service.ChatService
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.service.TradePostService
import com.wafflestudio.team03server.core.user.api.request.EditLocationRequest
import com.wafflestudio.team03server.core.user.api.request.EditPasswordRequest
import com.wafflestudio.team03server.core.user.api.request.EditUsernameRequest
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
@SpringBootTest
internal class UserServiceImplTest @Autowired constructor(
    val userRepository: UserRepository,
    val userService: UserService,
    val tradePostService: TradePostService,
    val chatService: ChatService,
    val passwordEncoder: PasswordEncoder
) {

    @Test
    fun 유저_정보_조회_성공() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        val savedUser = userRepository.save(user)
        //when
        val userResponse = userService.getProfile(savedUser.id)
        //then
        val findUser = userRepository.findByIdOrNull(savedUser.id) ?: throw Exception404("")
        assertThat(findUser.username).isEqualTo(userResponse.username)
        assertThat(findUser.email).isEqualTo(userResponse.email)
        assertThat(findUser.location).isEqualTo(userResponse.location)
        assertThat(findUser.temperature).isEqualTo(userResponse.temperature)
        assertThat(findUser.imgUrl).isEqualTo(userResponse.imgUrl)
        assertThat(findUser.createdAt).isEqualTo(userResponse.createdAt)
        assertThat(findUser.modifiedAt).isEqualTo(userResponse.modifiedAt)
    }

    @Test
    fun 유저_정보_조회_실패() {
        //given

        //when
        val exception = assertThrows(Exception404::class.java) {
            userService.getProfile(1)
        }
        //then
        assertThat(exception.message).isEqualTo("사용자를 찾을 수 없습니다.")
    }

    @Test
    fun 유저네임_수정_성공() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        val savedUser = userRepository.save(user)
        //when
        userService.editUsername(savedUser.id, EditUsernameRequest("Edited"))
        //then
        val findUser = userRepository.findByIdOrNull(savedUser.id) ?: throw Exception404("")
        assertThat(findUser.username).isEqualTo("Edited")
    }

    @Test
    fun 유저네임_수정_실패() {
        //given
        val user1 = User("user1", "a@naver.com", "1234", "송도동")
        val user2 = User("user2", "b@naver.com", "1234", "송도동")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        //when
        val exception = assertThrows(Exception409::class.java) {
            userService.editUsername(savedUser1.id, EditUsernameRequest("user2"))
        }
        //then
        assertThat(exception.message).isEqualTo("이미 존재하는 유저네임 입니다.")
    }

    @Test
    fun 주소_수정_성공() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        val savedUser = userRepository.save(user)
        //when
        userService.editLocation(savedUser.id, EditLocationRequest("Edited"))
        //then
        val findUser = userRepository.findByIdOrNull(savedUser.id) ?: throw Exception404("")
        assertThat(findUser.location).isEqualTo("Edited")
    }

    @Test
    fun 비밀번호_수정_성공() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        user.password = passwordEncoder.encode(user.password)
        val savedUser = userRepository.save(user)
        //when
        userService.editPassword(
            savedUser.id,
            EditPasswordRequest("1234", "Edited123!", "Edited123!")
        )
        //then
        val findUser = userRepository.findByIdOrNull(savedUser.id) ?: throw Exception404("")
        passwordEncoder.matches("Edited123!", findUser.password)
    }

    @Test
    fun 비밀번호_수정_실패_비밀번호_틀림() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        user.password = passwordEncoder.encode(user.password)
        val savedUser = userRepository.save(user)
        //when
        val exception = assertThrows(Exception403::class.java) {
            userService.editPassword(
                savedUser.id,
                EditPasswordRequest("wrongPw", "Edited123!", "Edited123!")
            )
        }
        //then
        assertThat(exception.message).isEqualTo("기존 비밀번호가 틀렸습니다.")
    }

    @Test
    fun 비밀번호_수정_실패_새비밀번호_불일치() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        user.password = passwordEncoder.encode(user.password)
        val savedUser = userRepository.save(user)
        //when
        val exception = assertThrows(Exception400::class.java) {
            userService.editPassword(
                savedUser.id,
                EditPasswordRequest("1234", "Edited123!", "NotIdentical!")
            )
        }
        //then
        assertThat(exception.message).isEqualTo("비밀번호가 일치하지 않습니다.")
    }

    @Test
    fun 글_구매_내역_조회_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)

        val request1 = CreatePostRequest("title1", "String1", 10000)
        val request2 = CreatePostRequest("title2", "String2", 10000)
        val request3 = CreatePostRequest("title3", "String3", 10000)
        val post1 = tradePostService.createPost(savedUser1.id, null, request1)
        val post2 = tradePostService.createPost(savedUser1.id, null, request2)
        val post3 = tradePostService.createPost(savedUser1.id, null, request3)

        // 예약 + 구매 확정
        chatService.startChat(savedUser2.id, post1.postId)
        chatService.startChat(savedUser2.id, post2.postId)
        chatService.startChat(savedUser3.id, post3.postId)
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post1.postId)
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post2.postId)
        tradePostService.changeBuyer(savedUser1.id, savedUser3.id, post3.postId)
        tradePostService.confirmTrade(savedUser1.id, post1.postId)
        tradePostService.confirmTrade(savedUser1.id, post2.postId)
        tradePostService.confirmTrade(savedUser1.id, post3.postId)

        // when
        val buyTradePosts = userService.getBuyTradePosts(savedUser2.id)

        // then
        assertThat(buyTradePosts.posts.size).isEqualTo(2)
        assertThat(buyTradePosts.posts[0].title).isEqualTo(post1.title)
    }

    @Test
    fun 글_판매_내역_조회_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)

        val request1 = CreatePostRequest("title1", "String1", 10000)
        val request2 = CreatePostRequest("title2", "String2", 10000)
        val request3 = CreatePostRequest("title3", "String3", 10000)
        val post1 = tradePostService.createPost(savedUser1.id, null, request1)
        val post2 = tradePostService.createPost(savedUser1.id, null, request2)
        val post3 = tradePostService.createPost(savedUser2.id, null, request3)

        // when
        val sellTradePosts = userService.getSellTradePosts(savedUser1.id, savedUser1.id)

        // then
        assertThat(sellTradePosts.posts.size).isEqualTo(2)
        assertThat(sellTradePosts.posts[0].title).isEqualTo(post1.title)
    }

    @Test
    fun 찜한_채팅목록_내역_조회_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)

        val request1 = CreatePostRequest("title1", "String1", 10000)
        val request2 = CreatePostRequest("title2", "String2", 20000)
        val request3 = CreatePostRequest("title3", "String3", 30000)
        val post1 = tradePostService.createPost(savedUser2.id, null, request1)
        val post2 = tradePostService.createPost(savedUser2.id, null, request2)
        val post3 = tradePostService.createPost(savedUser3.id, null, request3)

        tradePostService.likePost(savedUser1.id, post1.postId)
        tradePostService.likePost(savedUser1.id, post2.postId)

        // when
        val likeTradePosts = userService.getLikeTradePosts(savedUser1.id)

        // then
        assertThat(likeTradePosts.posts.size).isEqualTo(2)
        assertThat(likeTradePosts.posts[0].title).isEqualTo(post1.title)
        assertThat(likeTradePosts.posts[0].isLiked).isTrue
        assertThat(likeTradePosts.posts[1].isLiked).isTrue
    }

    @Test
    fun 내_채팅목록_내역_조회_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)

        val request = CreatePostRequest("title1", "String1", 10000)
        val post1 = tradePostService.createPost(savedUser1.id, null, request)
        val chat1 = chatService.startChat(savedUser2.id, post1.postId)
        val chat2 = chatService.startChat(savedUser3.id, post1.postId)
        val chatMessage = ChatMessage(chat1.roomUUID, savedUser2.id, "안녕하세요!", LocalDateTime.now())
        val chatMessage2 = ChatMessage(chat1.roomUUID, savedUser1.id, "안녕하세요?", LocalDateTime.now())
        val chatMessage3 = ChatMessage(chat2.roomUUID, savedUser1.id, "반갑습니다.", LocalDateTime.now())
        chatService.saveMessage(chatMessage)
        chatService.saveMessage(chatMessage2)
        chatService.saveMessage(chatMessage3)

        // when
        val mychats = userService.getMyChats(savedUser1.id)

        // then
        assertThat(mychats.chats.size).isEqualTo(2)
        assertThat(mychats.chats[0].buyer.id).isEqualTo(savedUser2.id)
        assertThat(mychats.chats[0].post.title).isEqualTo(request.title)
        assertThat(mychats.chats[0].roomUUID).isEqualTo(chat1.roomUUID)
        assertThat(mychats.chats[0].lastChat.message).isEqualTo(chatMessage2.message)
        assertThat(mychats.chats[1].roomUUID).isEqualTo(chat2.roomUUID)
        assertThat(mychats.chats[1].lastChat.message).isEqualTo(chatMessage3.message)
    }
}
