package com.wafflestudio.team03server.core.neighbor.service

import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.neighbor.api.request.CreateNeighborCommentRequest
import com.wafflestudio.team03server.core.neighbor.api.request.UpdateNeighborCommentRequest
import com.wafflestudio.team03server.core.neighbor.entity.NeighborComment
import com.wafflestudio.team03server.core.neighbor.repository.NeighborCommentRepository
import com.wafflestudio.team03server.core.neighbor.repository.NeighborPostRepository
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NeighborCommentService {
    fun createNeighborComment(userId: Long, postId: Long, createNeighborCommentRequest: CreateNeighborCommentRequest)
    fun updateNeighborComment(userId: Long, commentId: Long, updateNeighborCommentRequest: UpdateNeighborCommentRequest)
    fun deleteNeighborComment(userId: Long, commentId: Long)
}

@Service
@Transactional
class NeighborCommentServiceImpl(
    val userRepository: UserRepository,
    val neighborPostRepository: NeighborPostRepository,
    val neighborCommentRepository: NeighborCommentRepository
) : NeighborCommentService {

    private fun getUserById(userId: Long) =
        userRepository.findByIdOrNull(userId) ?: throw Exception404("유효한 회원이 아닙니다.")

    private fun getNeighborPostById(postId: Long) =
        neighborPostRepository.findByIdOrNull(postId) ?: throw Exception404("${postId}에 해당하는 글이 없습니다.")

    override fun createNeighborComment(
        userId: Long,
        postId: Long,
        createNeighborCommentRequest: CreateNeighborCommentRequest
    ) {
        val commenter = getUserById(userId)
        val post = getNeighborPostById(postId)
        val (comment, isHidden) = createNeighborCommentRequest
        val neighborComment =
            NeighborComment(neighborPost = post, commenter = commenter, comment = comment!!, isHidden = isHidden)
        neighborComment.mapNeighborPost(post)
        neighborCommentRepository.save(neighborComment)
    }

    private fun getCommentById(commentId: Long) =
        neighborCommentRepository.findByIdOrNull(commentId) ?: throw Exception404("${commentId}에 해당하는 댓글이 없습니다.")

    private fun checkCommenter(comment: NeighborComment, userId: Long) {
        if (comment.commenter.id != userId) throw Exception403("댓글 작성자에게만 권한이 있습니다.")
    }

    private fun updateCommentByRequest(comment: NeighborComment, request: UpdateNeighborCommentRequest) {
        comment.comment = request.comment ?: comment.comment
    }

    override fun updateNeighborComment(
        userId: Long,
        commentId: Long,
        updateNeighborCommentRequest: UpdateNeighborCommentRequest
    ) {
        val readComment = getCommentById(commentId)
        checkCommenter(readComment, userId)
        updateCommentByRequest(readComment, updateNeighborCommentRequest)
    }

    override fun deleteNeighborComment(userId: Long, commentId: Long) {
        val readComment = getCommentById(commentId)
        checkCommenter(readComment, userId)
        neighborCommentRepository.delete(readComment)
    }
}
