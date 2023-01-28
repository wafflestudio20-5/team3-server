package com.wafflestudio.team03server.core.neighbor.service

import com.wafflestudio.team03server.common.Exception400
import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.neighbor.api.request.CreateNeighborPostRequest
import com.wafflestudio.team03server.core.neighbor.api.request.UpdateNeighborPostRequest
import com.wafflestudio.team03server.core.neighbor.api.response.NeighborPostResponse
import com.wafflestudio.team03server.core.neighbor.entity.NeighborLike
import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.neighbor.repository.NeighborLikeRepository
import com.wafflestudio.team03server.core.neighbor.repository.NeighborPostRepository
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NeighborPostService {
    fun getAllNeighborPosts(userId: Long, neighborPostName: String?, pageable: Pageable): List<NeighborPostResponse>
    fun createNeighborPost(userId: Long, createNeighborPostRequest: CreateNeighborPostRequest): NeighborPostResponse
    fun getNeighborPost(userId: Long, postId: Long): NeighborPostResponse
    fun updateNeighborPost(
        userId: Long,
        postId: Long,
        updateNeighborPostRequest: UpdateNeighborPostRequest
    ): NeighborPostResponse

    fun deleteNeighborPost(userId: Long, postId: Long)
    fun likeOrUnlikeNeighborPost(userId: Long, postId: Long): NeighborPostResponse
}

@Service
@Transactional
class NeighborPostServiceImpl(
    val userRepository: UserRepository,
    val neighborPostRepository: NeighborPostRepository,
    val neighborLikeRepository: NeighborLikeRepository
) : NeighborPostService {

    override fun getAllNeighborPosts(
        userId: Long,
        neighborPostKeyword: String?,
        pageable: Pageable
    ): List<NeighborPostResponse> {
        neighborPostKeyword?.let {
            return neighborPostRepository.findAllByContentContains(neighborPostKeyword, pageable)
                .map { NeighborPostResponse.of(it, userId) }
        } ?: return neighborPostRepository.findAllByQuerydsl(pageable).map { NeighborPostResponse.of(it, userId) }
    }

    private fun getUserById(userId: Long) = userRepository.findByIdOrNull(userId) ?: throw Exception404("유효한 회원이 아닙니다.")

    override fun createNeighborPost(
        userId: Long,
        createNeighborPostRequest: CreateNeighborPostRequest
    ): NeighborPostResponse {
        val publisher = getUserById(userId)
        val content = createNeighborPostRequest.content
        val neighborPost = NeighborPost(content = content!!, publisher = publisher)
        neighborPostRepository.save(neighborPost)
        return NeighborPostResponse.of(neighborPost, userId)
    }

    private fun getNeighborPostById(postId: Long) =
        neighborPostRepository.findByIdOrNull(postId) ?: throw Exception404("${postId}에 해당하는 글이 없습니다.")

    private fun updateViewCount(post: NeighborPost) {
        post.viewCount += 1
    }

    override fun getNeighborPost(userId: Long, postId: Long): NeighborPostResponse {
        val readPost = getNeighborPostById(postId)
        updateViewCount(readPost)
        return NeighborPostResponse.of(readPost, userId)
    }

    private fun checkPublisher(readPost: NeighborPost, userId: Long) {
        if (readPost.publisher.id != userId) throw Exception403("글 작성자에게만 권한이 있습니다.")
    }

    private fun updateNeighborPostByRequest(post: NeighborPost, request: UpdateNeighborPostRequest) {
        post.content = request.content ?: post.content
    }

    override fun updateNeighborPost(
        userId: Long,
        postId: Long,
        updateNeighborPostRequest: UpdateNeighborPostRequest
    ): NeighborPostResponse {
        val readPost = getNeighborPostById(postId)
        checkPublisher(readPost, userId)
        updateNeighborPostByRequest(readPost, updateNeighborPostRequest)
        return NeighborPostResponse.of(readPost, userId)
    }

    override fun deleteNeighborPost(userId: Long, postId: Long) {
        val readPost = getNeighborPostById(postId)
        checkPublisher(readPost, userId)
        neighborPostRepository.delete(readPost)
    }

    private fun getNeighborLikeOrNull(user: User, post: NeighborPost): NeighborLike? {
        return neighborLikeRepository.findNeighborLikeByLikedPostAndLiker(liker = user, likedPost = post)
    }

    private fun checkPublisherEqualsLiker(user: User, post: NeighborPost) {
        if (post.publisher == user) throw Exception400("본인의 글에는 좋아요를 누를 수 없습니다.")
    }

    override fun likeOrUnlikeNeighborPost(userId: Long, postId: Long): NeighborPostResponse {
        val readUser = getUserById(userId)
        val readPost = getNeighborPostById(postId)
        checkPublisherEqualsLiker(readUser, readPost)

        val readLike = getNeighborLikeOrNull(readUser, readPost)
        if (readLike == null) {
            val newLike = NeighborLike(liker = readUser, likedPost = readPost)
            newLike.mapLikedPost(readPost)
            neighborLikeRepository.save(newLike)
        } else {
            readLike.changeStatus()
        }
        return NeighborPostResponse.of(readPost, userId)
    }
}
