package com.wafflestudio.team03server.core.neighbor.service

import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.neighbor.api.request.CreateNeighborReplyRequest
import com.wafflestudio.team03server.core.neighbor.api.request.UpdateNeighborReplyRequest
import com.wafflestudio.team03server.core.neighbor.entity.NeighborReply
import com.wafflestudio.team03server.core.neighbor.repository.NeighborCommentRepository
import com.wafflestudio.team03server.core.neighbor.repository.NeighborReplyRepository
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface NeighborReplyService {
    fun createNeighborReply(userId: Long, commentId: Long, createNeighborReplyRequest: CreateNeighborReplyRequest)
    fun updateNeighborReply(userId: Long, replyId: Long, updateNeighborReplyResponse: UpdateNeighborReplyRequest)
    fun deleteNeighborReply(userId: Long, replyId: Long)
}

@Service
@Transactional
class NeighborReplyServiceImpl(
    val userRepository: UserRepository,
    val neighborCommentRepository: NeighborCommentRepository,
    val neighborReplyRepository: NeighborReplyRepository
) : NeighborReplyService {

    private fun getUserById(userId: Long) =
        userRepository.findByIdOrNull(userId) ?: throw Exception404("유효한 회원이 아닙니다.")

    private fun getNeighborCommentById(commentId: Long) =
        neighborCommentRepository.findByIdOrNull(commentId) ?: throw Exception404("${commentId}에 해당하는 답글이 없습니다.")

    override fun createNeighborReply(
        userId: Long,
        commentId: Long,
        createNeighborReplyRequest: CreateNeighborReplyRequest
    ) {
        val replier = getUserById(userId)
        val comment = getNeighborCommentById(commentId)
        val message = createNeighborReplyRequest.message!!
        val neighborReply = NeighborReply(neighborComment = comment, replier = replier, replyingMessage = message)
        neighborReply.mapNeighborComment(comment)
        neighborReplyRepository.save(neighborReply)
    }

    private fun checkReplier(reply: NeighborReply, userId: Long) {
        if (reply.replier.id != userId) throw Exception403("답글 작성자에게만 권한이 있습니다.")
    }

    private fun getReplyById(replyId: Long) =
        neighborReplyRepository.findByIdOrNull(replyId) ?: throw Exception404("${replyId}에 해당하는 답글이 없습니다.")

    private fun updateReplyByRequest(reply: NeighborReply, request: UpdateNeighborReplyRequest) {
        reply.replyingMessage = request.message ?: reply.replyingMessage
    }

    override fun updateNeighborReply(
        userId: Long,
        replyId: Long,
        updateNeighborReplyResponse: UpdateNeighborReplyRequest
    ) {
        val readReply = getReplyById(replyId)
        checkReplier(readReply, userId)
        updateReplyByRequest(readReply, updateNeighborReplyResponse)
    }

    override fun deleteNeighborReply(userId: Long, replyId: Long) {
        val readReply = getReplyById(replyId)
        checkReplier(readReply, userId)
        neighborReplyRepository.delete(readReply)
    }
}
