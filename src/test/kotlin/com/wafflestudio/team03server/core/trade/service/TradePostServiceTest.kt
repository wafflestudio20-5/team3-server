package com.wafflestudio.team03server.core.trade.service

import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.entity.TradeState
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
    val tradePostService: TradePostService,
) {

    @Test
    fun 글_생성_성공() {
        // given
        val user = User("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)

        // when
        val (_, title, desc, price, seller, _, _, tradeStatus, _) = tradePostService.createPost(
            savedUser.id,
            request
        )

        // then
        assertThat(title).isEqualTo(request.title)
        assertThat(desc).isEqualTo(request.desc)
        assertThat(price).isEqualTo(request.price)
        assertThat(seller.id).isEqualTo(savedUser.id)
        assertThat(tradeStatus).isEqualTo(TradeState.TRADING)
    }

    @Test
    fun 글_단건_조회_성공() {
        // given
        val user = User("user1", "abc@naver.com", "1234", "관악구")
        val savedUser = userRepository.save(user)
        val request = CreatePostRequest("title1", "String1", 10000)

        val (postId, title, desc, price, seller, _, _, tradeStatus, _) = tradePostService.createPost(
            savedUser.id,
            request
        )

        // when
        val findPost = tradePostService.getPost(postId)

        // then
        assertThat(title).isEqualTo(findPost.title)
        assertThat(desc).isEqualTo(findPost.desc)
        assertThat(price).isEqualTo(findPost.price)
        assertThat(seller.id).isEqualTo(savedUser.id)
        assertThat(tradeStatus).isEqualTo(TradeState.TRADING)
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
        val posts = tradePostService.getPosts()

        // then
        assertThat(posts.size).isEqualTo(2)
    }
}