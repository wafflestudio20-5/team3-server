package com.wafflestudio.team03server.core.neighbor.service

import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.neighbor.api.request.CreateNeighborPostRequest
import com.wafflestudio.team03server.core.neighbor.api.request.UpdateNeighborPostRequest
import com.wafflestudio.team03server.core.neighbor.api.response.NeighborCommentResponse
import com.wafflestudio.team03server.core.neighbor.api.response.NeighborPostResponse
import com.wafflestudio.team03server.core.neighbor.entity.NeighborLike
import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.neighbor.repository.NeighborLikeRepository
import com.wafflestudio.team03server.core.neighbor.repository.NeighborPostRepository
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NeighborPostService {
    fun getAllNeighborPosts(): List<NeighborPostResponse>
    fun createNeighborPost(userId: Long, createNeighborPostRequest: CreateNeighborPostRequest): NeighborPostResponse
    fun getNeighborPost(postId: Long): NeighborPostResponse
    fun updateNeighborPost(
        userId: Long,
        postId: Long,
        updateNeighborPostRequest: UpdateNeighborPostRequest
    ): NeighborPostResponse

    fun deleteNeighborPost(userId: Long, postId: Long)
    fun likeNeighborPost(userId: Long, postId: Long)
}

@Service
@Transactional
class NeighborPostServiceImpl(
    val userRepository: UserRepository,
    val neighborPostRepository: NeighborPostRepository,
    val neighborLikeRepository: NeighborLikeRepository
) : NeighborPostService {

    override fun getAllNeighborPosts(): List<NeighborPostResponse> {
        return neighborPostRepository.findAll().map { NeighborPostResponse.of(it) }
    }

    private fun getUserById(userId: Long) = userRepository.findByIdOrNull(userId) ?: throw Exception404("유효한 회원이 아닙니다.")

    override fun createNeighborPost(
        userId: Long,
        createNeighborPostRequest: CreateNeighborPostRequest
    ): NeighborPostResponse {
        val publisher = getUserById(userId)
        val (title, content) = createNeighborPostRequest
        val neighborPost = NeighborPost(title = title, content = content, publisher = publisher)
        neighborPostRepository.save(neighborPost)
        return NeighborPostResponse(
            postId = neighborPost.id,
            title = neighborPost.title,
            content = neighborPost.content,
            publisher = SimpleUserResponse.of(neighborPost.publisher),
            comments = neighborPost.comments.map { NeighborCommentResponse.of(it) },
            viewCount = neighborPost.viewCount,
            createdAt = neighborPost.createdAt
        )
    }

    private fun getNeighborPostById(postId: Long) =
        neighborPostRepository.findByIdOrNull(postId) ?: throw Exception404("${postId}에 해당하는 글이 없습니다.")

    private fun updateViewCount(post: NeighborPost) {
        post.viewCount += 1
    }

    override fun getNeighborPost(postId: Long): NeighborPostResponse {
        val readPost = getNeighborPostById(postId)
        updateViewCount(readPost)
        return NeighborPostResponse(
            postId = readPost.id,
            title = readPost.title,
            content = readPost.content,
            publisher = SimpleUserResponse.of(readPost.publisher),
            comments = readPost.comments.map { NeighborCommentResponse.of(it) },
            viewCount = readPost.viewCount,
            createdAt = readPost.createdAt
        )
    }

    private fun checkPublisher(readPost: NeighborPost, userId: Long) {
        if (readPost.publisher.id != userId) throw Exception403("글 작성자에게만 권한이 있습니다.")
    }

    private fun updateNeighborPostByRequest(post: NeighborPost, request: UpdateNeighborPostRequest) {
        post.title = request.title ?: post.title
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
        return NeighborPostResponse.of(readPost)
    }

    override fun deleteNeighborPost(userId: Long, postId: Long) {
        val readPost = getNeighborPostById(postId)
        checkPublisher(readPost, userId)
        neighborPostRepository.delete(readPost)
    }

    override fun likeNeighborPost(userId: Long, postId: Long) {
        val readUser = getUserById(userId)
        val readPost = getNeighborPostById(postId)
        val newLike = NeighborLike(liker = readUser, likedPost = readPost)
        neighborLikeRepository.save(newLike)
    }
}
