package com.wafflestudio.team03server.core.trade.service

import com.wafflestudio.team03server.core.chat.service.ChatService
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.request.UpdatePostRequest
import com.wafflestudio.team03server.core.trade.entity.TradeState
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
internal class TradePostServiceTest @Autowired constructor(
    val userRepository: UserRepository,
    val tradePostRepository: TradePostRepository,
    val tradePostService: TradePostService,
    val chatService: ChatService,
) {

    @Test
    fun 글_생성_성공() {
        // given
        val user = User("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)

        // when
        val response = tradePostService.createPost(savedUser.id, request)

        // then
        assertThat(response.title).isEqualTo(request.title)
        assertThat(response.desc).isEqualTo(request.desc)
        assertThat(response.price).isEqualTo(request.price)
        assertThat(response.seller.id).isEqualTo(savedUser.id)
        assertThat(response.buyer).isNull()
        assertThat(response.reservationCount).isEqualTo(0)
        assertThat(response.viewCount).isEqualTo(0)
        assertThat(response.likeCount).isEqualTo(0)
        assertThat(response.isLiked).isFalse
        assertThat(response.isOwner).isTrue
        assertThat(response.tradeStatus).isEqualTo(TradeState.TRADING)
    }

    @Test
    fun 글_단건_조회_성공() {
        // given
        val user = User("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)
        val createPost = tradePostService.createPost(savedUser.id, request)

        // when
        val findPost = tradePostService.getPost(savedUser.id, createPost.postId)

        // then
        assertThat(createPost.title).isEqualTo(findPost.title)
        assertThat(createPost.desc).isEqualTo(findPost.desc)
        assertThat(createPost.price).isEqualTo(findPost.price)
        assertThat(createPost.seller.id).isEqualTo(savedUser.id)
        assertThat(createPost.buyer).isNull()
        assertThat(findPost.isOwner).isTrue
        assertThat(findPost.viewCount).isEqualTo(1)
        assertThat(createPost.tradeStatus).isEqualTo(TradeState.TRADING)
    }

    @Test
    fun 글_전부_조회_성공() {
        // given
        val user = User("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)
        val request2 = CreatePostRequest("title2", "String2", 20000)
        tradePostService.createPost(savedUser.id, request)
        tradePostService.createPost(savedUser.id, request2)

        // when
        val posts = tradePostService.getPosts(savedUser.id)

        // then
        assertThat(posts.size).isEqualTo(2)
    }

    @Test
    fun 글_수정_성공() {
        // given
        val user = User("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "desc1", 10000)
        val createdPost = tradePostService.createPost(savedUser.id, request)

        // when
        val updateRequest = UpdatePostRequest("변경된 title", null, 20000)
        val updatePost = tradePostService.updatePost(savedUser.id, createdPost.postId, updateRequest)

        // then
        assertThat(updatePost.title).isEqualTo("변경된 title")
        assertThat(updatePost.desc).isEqualTo(createdPost.desc)
        assertThat(updatePost.price).isEqualTo(updateRequest.price)
    }

    @Test
    fun 글_삭제_성공() {
        // given
        val user = User("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "desc1", 10000)
        val createdPost = tradePostService.createPost(savedUser.id, request)

        // when
        tradePostService.removePost(savedUser.id, createdPost.postId)

        //then
        val posts = tradePostService.getPosts(savedUser.id)
        assertThat(posts.size).isEqualTo(0)
    }

    @Test
    fun 글_예약자_가져오기_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        val reservations = tradePostService.getReservations(savedUser1.id, post.postId)

        // then
        assertThat(reservations.tradeState).isEqualTo(TradeState.TRADING)
        assertThat(reservations.buyer).isNull()
        assertThat(reservations.candidates.size).isEqualTo(2)
    }

    @Test
    fun 글_예약자_선정하기_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post.postId)
        val findPost = tradePostService.getPost(savedUser1.id, post.postId)

        // then
        assertThat(findPost.isOwner).isTrue
        assertThat(findPost.reservationCount).isEqualTo(2)
        assertThat(findPost.buyer!!.email).isEqualTo(savedUser2.email)
        assertThat(findPost.tradeStatus).isEqualTo(TradeState.RESERVATION)
    }

    @Test
    fun 글_예약자_변경하기_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post.postId)
        tradePostService.changeBuyer(savedUser1.id, savedUser3.id, post.postId)
        val findPost = tradePostService.getPost(savedUser1.id, post.postId)

        // then
        assertThat(findPost.isOwner).isTrue
        assertThat(findPost.reservationCount).isEqualTo(2)
        assertThat(findPost.buyer!!.email).isEqualTo(savedUser3.email)
        assertThat(findPost.tradeStatus).isEqualTo(TradeState.RESERVATION)
    }

    @Test
    fun 글_거래_확정_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post.postId)
        tradePostService.confirmTrade(savedUser1.id, post.postId)
        val findPost = tradePostService.getPost(savedUser1.id, post.postId)

        // then
        assertThat(findPost.isOwner).isTrue
        assertThat(findPost.tradeStatus).isEqualTo(TradeState.COMPLETED)
    }

    @Test
    fun 글_예약_취소_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = User("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post.postId)
        tradePostService.cancelTrade(savedUser1.id, post.postId)
        val findPost = tradePostService.getPost(savedUser1.id, post.postId)

        // then
        assertThat(findPost.tradeStatus).isEqualTo(TradeState.TRADING)
        assertThat(findPost.buyer).isNull()
    }

    @Test
    fun 글_찜_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, request)

        // when
        tradePostService.likePost(savedUser2.id, post.postId)
        val findPost = tradePostRepository.findById(post.postId).get()
        val findUser = userRepository.findById(savedUser2.id).get()

        // then
        assertThat(findPost.likeTradePosts.size).isEqualTo(1)
        assertThat(findPost.likeTradePosts[0].user.id).isEqualTo(savedUser2.id)
        assertThat(findPost.likeTradePosts[0].likedPost.id).isEqualTo(post.postId)
        assertThat(findUser.likeTradePosts[0].likedPost.id).isEqualTo(post.postId)
    }

    @Test
    fun 글_찜_취소_성공() {
        // given
        val user1 = User("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = User("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, request)

        // when
        tradePostService.likePost(savedUser2.id, post.postId)
        tradePostService.likePost(savedUser2.id, post.postId)
        val findPost = tradePostRepository.findById(post.postId).get()
        val findUser = userRepository.findById(savedUser2.id).get()

        // then
        assertThat(findPost.likeTradePosts.size).isEqualTo(0)
        assertThat(findUser.likeTradePosts.size).isEqualTo(0)
    }
}
