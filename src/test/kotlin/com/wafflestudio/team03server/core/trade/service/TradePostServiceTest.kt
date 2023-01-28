package com.wafflestudio.team03server.core.trade.service

import com.wafflestudio.team03server.core.chat.service.ChatService
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.request.UpdatePostRequest
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import javax.transaction.Transactional

@SpringBootTest
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
        val user = createUser("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)

        // when
        val response = tradePostService.createPost(savedUser.id, null, request)

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
        assertThat(response.tradeStatus).isEqualTo(TradeStatus.TRADING)
    }

    @Test
    fun 글_단건_조회_성공() {
        // given
        val user = createUser("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)
        val createPost = tradePostService.createPost(savedUser.id, null, request)

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
        assertThat(createPost.tradeStatus).isEqualTo(TradeStatus.TRADING)
    }

    @Test
    fun 글_전부_조회_성공() {
        // given
        val user = createUser("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)
        val request2 = CreatePostRequest("title2", "String2", 20000)
        tradePostService.createPost(savedUser.id, null, request)
        tradePostService.createPost(savedUser.id, null, request2)

        // when
        val posts = tradePostService.getAllPosts(savedUser.id, null, PageRequest.of(0, 10))

        // then
        assertThat(posts.posts.size).isEqualTo(2)
    }

    @Test
    fun 글_페이지네이션_조회_성공() {
        val user = createUser("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        for (i in 0..99) {
            val request = CreatePostRequest("title$i", "String$i", i)
            tradePostService.createPost(savedUser.id, null, request)
        }

        val allPosts = tradePostService.getAllPosts(savedUser.id, null, PageRequest.of(0, 10))

        assertThat(allPosts.posts.size).isEqualTo(10)
        assertThat(allPosts.posts[0].title).isEqualTo("title99")
        assertThat(allPosts.posts[0].title).isEqualTo("title99")
        assertThat(allPosts.paging.limit).isEqualTo(10)
        assertThat(allPosts.paging.offset).isEqualTo(0)
        assertThat(allPosts.paging.total).isEqualTo(100)
    }

    @Test
    fun 글_수정_성공() {
        // given
        val user = createUser("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "desc1", 10000)
        val createdPost = tradePostService.createPost(savedUser.id, null, request)

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
        val user = createUser("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "desc1", 10000)
        val createdPost = tradePostService.createPost(savedUser.id, null, request)

        // when
        tradePostService.removePost(savedUser.id, createdPost.postId)

        //then
        val posts = tradePostService.getAllPosts(savedUser.id, null, PageRequest.of(0, 10))
        assertThat(posts.posts.size).isEqualTo(0)
    }

    @Test
    fun 글_예약자_가져오기_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = createUser("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, null, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        val reservations = tradePostService.getReservations(savedUser1.id, post.postId)

        // then
        assertThat(reservations.tradeStatus).isEqualTo(TradeStatus.TRADING)
        assertThat(reservations.buyer).isNull()
        assertThat(reservations.candidates.size).isEqualTo(2)
        assertThat(reservations.candidates[0].email).isEqualTo(savedUser2.email)
        assertThat(reservations.candidates[0].roomUUID).isEqualTo(chat1_1.roomUUID)
    }

    @Test
    fun 글_예약자_선정하기_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = createUser("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, null, request)
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
        assertThat(findPost.tradeStatus).isEqualTo(TradeStatus.RESERVATION)
    }

    @Test
    fun 글_예약자_변경하기_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = createUser("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, null, request)
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
        assertThat(findPost.tradeStatus).isEqualTo(TradeStatus.RESERVATION)
    }

    @Test
    fun 글_거래_확정_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = createUser("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, null, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post.postId)
        tradePostService.confirmTrade(savedUser1.id, post.postId)
        val findPost = tradePostService.getPost(savedUser1.id, post.postId)

        // then
        assertThat(findPost.isOwner).isTrue
        assertThat(findPost.tradeStatus).isEqualTo(TradeStatus.COMPLETED)
    }

    @Test
    fun 글_예약_취소_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = createUser("user3", "abc3@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, null, request)
        val chat1_1 = chatService.startChat(savedUser2.id, post.postId) // 첫 채팅
        val chat1_2 = chatService.startChat(savedUser2.id, post.postId) // 같은 사용자 두 번째 채팅
        val chat2 = chatService.startChat(savedUser3.id, post.postId)

        // when
        tradePostService.changeBuyer(savedUser1.id, savedUser2.id, post.postId)
        tradePostService.cancelTrade(savedUser1.id, post.postId)
        val findPost = tradePostService.getPost(savedUser1.id, post.postId)

        // then
        assertThat(findPost.tradeStatus).isEqualTo(TradeStatus.TRADING)
        assertThat(findPost.buyer).isNull()
    }

    @Test
    fun 글_찜_성공() {
        // given
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, null, request)

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
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val request = CreatePostRequest("title1", "String1", 10000)
        val post = tradePostService.createPost(savedUser1.id, null, request)

        // when
        tradePostService.likePost(savedUser2.id, post.postId)
        tradePostService.likePost(savedUser2.id, post.postId)
        val findPost = tradePostRepository.findById(post.postId).get()
        val findUser = userRepository.findById(savedUser2.id).get()

        // then
        assertThat(findPost.likeTradePosts.size).isEqualTo(0)
        assertThat(findUser.likeTradePosts.size).isEqualTo(0)
    }

    @Test
    fun 인기_탑3_글_조회_성공() {
        // given
        // 유저 생성
        val user1 = createUser("user1", "abc1@naver.com", "1234", "관악구")
        val user2 = createUser("user2", "abc2@naver.com", "1234", "관악구")
        val user3 = createUser("user3", "abc3@naver.com", "1234", "관악구")
        val user4 = createUser("user4", "abc4@naver.com", "1234", "관악구")
        val user5 = createUser("user5", "abc5@naver.com", "1234", "관악구")
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val savedUser4 = userRepository.save(user4)
        val savedUser5 = userRepository.save(user5)

        // 글 생성
        val request1 = CreatePostRequest("title1", "String1", 10000)
        val request2 = CreatePostRequest("title2", "String2", 10000)
        val request3 = CreatePostRequest("title3", "String3", 10000)
        val request4 = CreatePostRequest("title4", "String4", 10000)
        val request5 = CreatePostRequest("title5", "String5", 10000)
        val post1 = tradePostService.createPost(savedUser1.id, null, request1)
        val post2 = tradePostService.createPost(savedUser2.id, null, request2)
        val post3 = tradePostService.createPost(savedUser2.id, null, request3)
        val post4 = tradePostService.createPost(savedUser3.id, null, request4)
        val post5 = tradePostService.createPost(savedUser4.id, null, request5)

        // when
        // 글 찜 누르기
        tradePostService.likePost(savedUser1.id, post3.postId)
        tradePostService.likePost(savedUser3.id, post3.postId)
        tradePostService.likePost(savedUser4.id, post3.postId)
        tradePostService.likePost(savedUser5.id, post3.postId)

        tradePostService.likePost(savedUser1.id, post2.postId)
        tradePostService.likePost(savedUser3.id, post2.postId)
        tradePostService.likePost(savedUser4.id, post2.postId)

        tradePostService.likePost(savedUser1.id, post4.postId)
        tradePostService.likePost(savedUser2.id, post4.postId)
        tradePostService.likePost(savedUser4.id, post4.postId)

        tradePostService.likePost(savedUser1.id, post5.postId)
        tradePostService.likePost(savedUser2.id, post5.postId)

        // post2는 거래 완료 상태로 만들기
        chatService.startChat(savedUser3.id, post2.postId)
        tradePostService.changeBuyer(savedUser2.id, savedUser3.id, post2.postId)
        tradePostService.confirmTrade(savedUser2.id, post2.postId)
        val topThreePosts = tradePostService.getTopThreePosts(savedUser1.id)

        // then
        assertThat(topThreePosts.posts.size).isEqualTo(3)
        assertThat(topThreePosts.posts[0].postId).isEqualTo(post3.postId)
        assertThat(topThreePosts.posts[0].title).isEqualTo(post3.title)
        assertThat(topThreePosts.posts[0].likeCount).isGreaterThan(topThreePosts.posts[1].likeCount)
        assertThat(topThreePosts.posts.map { it.postId }).containsExactly(post3.postId, post4.postId, post5.postId)
    }

    private fun createUser(username: String, email: String, password: String, location: String): User {
        return User(username, email, password, location, WKTReader().read("POINT(1.0 1.0)") as Point)
    }
}
