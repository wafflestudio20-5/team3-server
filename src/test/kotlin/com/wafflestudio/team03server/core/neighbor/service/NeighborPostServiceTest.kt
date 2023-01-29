package com.wafflestudio.team03server.core.neighbor.service

import com.wafflestudio.team03server.core.neighbor.api.request.CreateNeighborPostRequest
import com.wafflestudio.team03server.core.neighbor.api.request.UpdateNeighborPostRequest
import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.neighbor.repository.NeighborLikeRepository
import com.wafflestudio.team03server.core.neighbor.repository.NeighborPostRepository
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.locationtech.jts.geom.Point
import org.locationtech.jts.io.WKTReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class NeighborPostServiceTest @Autowired constructor(
    val userRepository: UserRepository,
    val neighborPostRepository: NeighborPostRepository,
    val neighborPostService: NeighborPostService,
    val neighborLikeRepository: NeighborLikeRepository
) {
    @BeforeEach
    private fun deleteAll() {
        userRepository.deleteAll()
        neighborPostRepository.deleteAll()
    }

    @Test
    @DisplayName("게시글 조회 성공")
    fun getAllNeighborPosts() {
        // given
        val user1 = createUser("user1", "user1@me.com", "abcd!1234")
        (1..10).map { createPost("제목$it", "내용", user1) }

        // when
        val pageable = PageRequest.of(0, 50)
        val neighborPosts = neighborPostService.getAllNeighborPosts(user1.id, "", pageable)

        // then
        assertThat(neighborPosts.size).isEqualTo(10)
    }

    @Test
    @DisplayName("게시글 검색 성공")
    fun searchNeighborPosts() {
        // given
        val user1 = createUser("user1", "user1@me.com", "abcd!1234")
        (1..10).map { createPost("제목$it", "내용", user1) }
        val targetPost = createPost("삼겹살", "맛있다", user1)

        // when
        val pageable = PageRequest.of(0, 50)
        val neighborPosts = neighborPostService.getAllNeighborPosts(user1.id, "맛있다", pageable)

        // then
        assertThat(neighborPosts.size).isEqualTo(1)
    }

    @Test
    @DisplayName("게시글 생성 성공")
    fun createNeighborPost() {
        // given
        val user1 = createUser("user1", "user1@me.com", "abcd!1234")
        val request = CreateNeighborPostRequest("내용")

        // when
        val response = neighborPostService.createNeighborPost(user1.id, request)

        // then
//        assertThat(response.title).isEqualTo(request.title)
        assertThat(response.content).isEqualTo(request.content)
        assertThat(response.publisher.id).isEqualTo(user1.id)
        assertThat(response.comments.size).isEqualTo(0)
        assertThat(response.likeCount).isEqualTo(0)
        assertThat(response.viewCount).isEqualTo(0)
    }

    @Test
    @DisplayName("게시글 상세 조회 성공")
    fun getNeighborPost() {
        // given
        val user1 = createUser("user1", "user1@me.com", "abcd!1234")
        (1..10).map { createPost("제목$it", "내용", user1) }
        val targetPost = createPost("삼겹살", "맛있다", user1)

        // when
        val findPost = neighborPostService.getNeighborPost(user1.id, targetPost.id)

        // then
        assertThat(findPost.postId).isEqualTo(targetPost.id)
//        assertThat(findPost.title).isEqualTo(targetPost.title)
        assertThat(findPost.content).isEqualTo(targetPost.content)
        assertThat(findPost.publisher.id).isEqualTo(user1.id)
        assertThat(findPost.comments.size).isEqualTo(targetPost.comments.size)
        assertThat(findPost.likeCount).isEqualTo(targetPost.likes.size)
        assertThat(findPost.viewCount).isEqualTo(targetPost.viewCount)
    }

    @Test
    @DisplayName("게시글 수정 성공")
    fun updateNeighborPost() {
        // given
        val user1 = createUser("user1", "user1@me.com", "abcd!1234")
        (1..10).map { createPost("제목$it", "내용", user1) }
        val targetPost = createPost("삼겹살", "맛있다", user1)

        // when
        val request = UpdateNeighborPostRequest("괜찮다")
        val updatedPost = neighborPostService.updateNeighborPost(user1.id, targetPost.id, request)

        // then
        assertThat(updatedPost.postId).isEqualTo(targetPost.id)
//        assertThat(updatedPost.title).isEqualTo(request.title)
        assertThat(updatedPost.content).isEqualTo(request.content)
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    fun deleteNeighborPost() {
        // given
        val user1 = createUser("user1", "user1@me.com", "abcd!1234")
        (1..10).map { createPost("제목$it", "내용", user1) }
        val targetPost = createPost("삼겹살", "맛있다", user1)

        // when
        neighborPostService.deleteNeighborPost(user1.id, targetPost.id)

        // then
        val pageable = PageRequest.of(0, 50)
        val posts = neighborPostService.getAllNeighborPosts(user1.id, "", pageable)
        assertThat(posts.size).isEqualTo(10)
    }

    @Test
    @DisplayName("게시글 좋아요 성공")
    fun likeOrUnlikeNeighborPost() {
        // given
        val user1 = createUser("user1", "user1@me.com", "abcd!1234")
        val user2 = createUser("user2", "user2@me.com", "abcd!1234")
        val post = createPost("제목", "내용", user1)

        // when
        neighborPostService.likeOrUnlikeNeighborPost(user2.id, post.id)
        val like = neighborLikeRepository.findNeighborLikeByLikedPostAndLiker(post, user2)

        // then
        assertThat(like).isNotNull
        assertThat(post.likes.size).isEqualTo(1)
        assertThat(post.likes[0].liker.id).isEqualTo(user2.id)
        assertThat(post.likes[0].likedPost.id).isEqualTo(post.id)
    }

    private fun createUser(username: String, email: String, password: String, location: String = "관악구"): User {
        val user = User(username, email, password, location, WKTReader().read("POINT(1.0 1.0)") as Point)
        userRepository.save(user)
        return user
    }

    private fun createPost(title: String, content: String, user: User): NeighborPost {
        val post = NeighborPost(content, user)
        neighborPostRepository.save(post)
        return post
    }
}
