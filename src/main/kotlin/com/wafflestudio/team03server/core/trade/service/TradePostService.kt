package com.wafflestudio.team03server.core.trade.service

import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.trade.api.request.CreatePostRequest
import com.wafflestudio.team03server.core.trade.api.request.UpdatePostRequest
import com.wafflestudio.team03server.core.trade.api.response.PostResponse
import com.wafflestudio.team03server.core.trade.api.response.ReservationResponse
import com.wafflestudio.team03server.core.trade.entity.LikePost
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeState
import com.wafflestudio.team03server.core.trade.repository.LikePostRepository
import com.wafflestudio.team03server.core.trade.repository.TradePostRepository
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class TradePostService(
    val userRepository: UserRepository,
    val tradePostRepository: TradePostRepository,
    val likePostRepository: LikePostRepository,
) {
    fun createPost(userId: Long, request: CreatePostRequest): PostResponse {
        val (user, post) = makePost(userId, request)
        return PostResponse(
            postId = post.id,
            title = post.title,
            desc = post.description,
            price = post.price,
            seller = SimpleUserResponse.of(user),
            isOwner = true,
        )
    }

    private fun makePost(userId: Long, request: CreatePostRequest): Pair<User, TradePost> {
        val findUser = getUserById(userId)
        val (title, desc, price) = request
        val post = TradePost(title, desc, price, findUser)
        val savedPost = tradePostRepository.save(post)
        findUser.sellPosts.add(savedPost)
        return Pair(findUser, savedPost)
    }

    private fun getUserById(userId: Long) = userRepository.findByIdOrNull(userId) ?: throw Exception404("유효한 회원이 아닙니다.")

    fun getPost(userId: Long, postId: Long): PostResponse {
        val findUser = getUserById(userId)
        val findPost = getPostById(postId)
        findPost.viewCount++
        return PostResponse.of(findPost, findUser)
    }

    private fun getPostById(postId: Long) =
        tradePostRepository.findByIdOrNull(postId) ?: throw Exception404("ID: ${postId}에 해당하는 글이 없습니다.")

    fun getPosts(userId: Long): List<PostResponse> {
        val findUser = getUserById(userId)
        return tradePostRepository.findAll() // TODO: 추후 N + 1, Paging 적용하기
            .map { PostResponse.of(it, findUser) }
    }

    fun updatePost(userId: Long, postId: Long, request: UpdatePostRequest): PostResponse {
        val findUser = getUserById(userId)
        val findPost = getPostById(postId)
        checkPostOwner(findPost, userId)
        updatePostByRequest(findPost, request)
        return PostResponse.of(findPost, findUser)
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
        if (findPost.seller.id != userId) throw Exception403("글 작성자 권한이 필요합니다.")
    }

    fun removePost(userId: Long, postId: Long) {
        val findUser = getUserById(userId)
        val findPost = getPostById(postId)
        checkPostOwner(findPost, userId)
        tradePostRepository.delete(findPost)
    }

    fun getReservations(userId: Long, postId: Long): ReservationResponse {
        val findPost = getPostById(postId)
        checkPostOwner(findPost, userId)
        val reservations = findPost.reservations
        return ReservationResponse.of(findPost)
    }

    fun changeBuyer(sellerId: Long, buyerId: Long, postId: Long) {
        val seller = getUserById(sellerId)
        val buyer = getUserById(buyerId)
        val post = getPostById(postId)
        checkPostOwner(post, sellerId)
        post.buyer = buyer
        post.tradeState = TradeState.RESERVATION
    }

    fun confirmTrade(sellerId: Long, postId: Long) {
        val seller = getUserById(sellerId)
        val post = getPostById(postId)
        checkValidConfirm(post, seller)
        post.tradeState = TradeState.COMPLETED
    }

    fun cancelTrade(sellerId: Long, postId: Long) {
        val seller = getUserById(sellerId)
        val post = getPostById(postId)
        checkValidCancel(seller, post)
        makeTradeStatus(post)
    }

    private fun makeTradeStatus(post: TradePost) {
        post.tradeState = TradeState.TRADING
        post.buyer = null
    }

    private fun checkValidCancel(seller: User, post: TradePost) {
        checkPostOwner(post, seller.id)
        if (notReservationState(post)) throw Exception400("예약중인 게시글이 아닙니다.")
    }

    private fun notReservationState(post: TradePost) =
        post.tradeState != TradeState.RESERVATION

    private fun checkValidConfirm(post: TradePost, seller: User) {
        checkPostOwner(post, seller.id)
        if (canNotConfirmTrade(post)) throw Exception400("예약중인 구매자가 없습니다.")
    }

    private fun canNotConfirmTrade(post: TradePost) =
        post.tradeState == TradeState.TRADING || post.buyer == null

    fun likePost(userId: Long, postId: Long) {
        val user = getUserById(userId)
        val post = getPostById(postId)
        checkNotPostOwner(user, post)
        val like = likePostRepository.findLikePostByUserAndLikedPost(user, post)
        like?.let {
            post.likeTradePosts.remove(it)
            likePostRepository.delete(it)
        } ?: let {
            val likePost = LikePost(user, post)
            post.likeTradePosts.add(likePost)
            likePostRepository.save(likePost)
        }
    }

    private fun checkNotPostOwner(user: User, post: TradePost) {
        if (user.id == post.seller.id) throw Exception400("자신의 거래글은 찜할수없습니다.")
    }
}
