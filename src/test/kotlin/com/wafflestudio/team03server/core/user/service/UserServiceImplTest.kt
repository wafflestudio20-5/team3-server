package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.core.chat.service.ChatService
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.trade.service.TradePostService
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
@SpringBootTest
internal class UserServiceImplTest @Autowired constructor(
    val userRepository: UserRepository,
    val userService: UserService,
    val tradePostRepository: TradePostRepository,
    val tradePostService: TradePostService,
    val chatService: ChatService,
) {

    @Test
    fun 유저_정보_조회_성공() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        val savedUser = userRepository.save(user)
        //when
        val userResponse = userService.getProfile(savedUser.id)
        //then
        assertThat(user.username).isEqualTo(userResponse.username)
        assertThat(user.email).isEqualTo(userResponse.email)
        assertThat(user.location).isEqualTo(userResponse.location)
        assertThat(user.temperature).isEqualTo(userResponse.temperature)
        assertThat(user.imgUrl).isEqualTo(userResponse.imgUrl)
        assertThat(user.createdAt).isEqualTo(userResponse.createdAt)
        assertThat(user.modifiedAt).isEqualTo(userResponse.modifiedAt)
    }

    @Test
    fun 유저_정보_조회_실패() {
        //given

        //when
        val userResponse = userService.getProfile(1)
        //then

    }

    @Test
    fun editUsername() {
        //given

        //when

        //then
    }

    @Test
    fun editLocation() {
        //given

        //when

        //then
    }

    @Test
    fun editPassword() {
        //given

        //when

        //then
    }

    @Test
    fun uploadImage() {
        //given

        //when

        //then
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
}
