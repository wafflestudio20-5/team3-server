package com.wafflestudio.team03server.core.trade.service


import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.request.UpdatePostRequest
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
        val findPost = getPostById(postId)
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

    private fun getPostById(postId: Long) =
        tradePostRepository.findByIdOrNull(postId) ?: throw Exception404("ID: ${postId}에 해당하는 글이 없습니다.")

    fun getPosts(): List<PostResponse> {
        return tradePostRepository.findAll() // 추후 N + 1, Paging 적용하기
            .map { PostResponse.of(it) }
    }
    
    fun updatePost(userId: Long, postId: Long, request: UpdatePostRequest): PostResponse {
        val findUser = getUserById(userId)
        val findPost = getPostById(postId)
        checkPostOwner(findPost, userId)
        updatePostByRequest(findPost, request)
        return PostResponse.of(findPost)
    }

    private fun updatePostByRequest(
        findPost: TradePost,
        request: UpdatePostRequest
    ) {
        findPost.title = request.title ?: findPost.title
        findPost.description = request.desc ?: findPost.description
        findPost.price = request.price ?: findPost.price
    }

    private fun checkPostOwner(findPost: TradePost, userId: Long) {
        if (findPost.seller.id != userId) throw Exception403("글 수정은 글 작성자만 가능합니다.")
    }

    fun removePost(userId: Long, postId: Long) {
        val findUser = getUserById(userId)
        val findPost = getPostById(postId)
        checkPostOwner(findPost, userId)
        tradePostRepository.delete(findPost)
    }
    
}
