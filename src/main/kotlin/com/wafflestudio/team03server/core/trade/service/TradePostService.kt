package com.wafflestudio.team03server.core.trade.service

import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.response.PostResponse
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class TradePostService(
    val userRepository: UserRepository,
    val tradePostRepository: TradePostRepository,
) {
    fun createPost(userId: Long, request: CreatePostRequest): PostResponse {
        val findUser = getUserById(userId)
        val (title, desc, price) = request
        val post = TradePost(title, desc, price, findUser)
        tradePostRepository.save(post)
        return PostResponse(
            postId = post.id,
            title = post.title,
            desc = post.description,
            price = post.price,
            seller = SimpleUserResponse.of(findUser)
        )
    }

    private fun getUserById(userId: Long) = userRepository.findByIdOrNull(userId) ?: throw Exception404("유효한 회원이 아닙니다.")

    fun getPost(postId: Long): PostResponse {
        val findPost = tradePostRepository.findByIdOrNull(postId) ?: throw Exception404("ID: ${postId}에 해당하는 글이 없습니다.")
        return PostResponse(
            postId = findPost.id,
            title = findPost.title,
            desc = findPost.description,
            price = findPost.price,
            seller = SimpleUserResponse.of(findPost.seller),
            buyer = findPost.buyer?.let { SimpleUserResponse.of(it) },
            reservationCount = findPost.reservations.size, // 추후 N + 1 문제 고려해서 리팩토링
            tradeStatus = findPost.tradeState,
            viewCount = findPost.viewCount
        )
    }

    fun getPosts(): List<PostResponse> {
        return tradePostRepository.findAll() // 추후 N + 1, Paging 적용하기
            .map { PostResponse.of(it) }
    }
}
