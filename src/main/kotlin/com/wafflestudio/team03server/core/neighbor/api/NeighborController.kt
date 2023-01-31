package com.wafflestudio.team03server.core.neighbor.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.neighbor.api.request.*
import com.wafflestudio.team03server.core.neighbor.api.response.NeighborPostPageResponse
import com.wafflestudio.team03server.core.neighbor.api.response.NeighborPostResponse
import com.wafflestudio.team03server.core.neighbor.service.NeighborCommentService
import com.wafflestudio.team03server.core.neighbor.service.NeighborPostService
import com.wafflestudio.team03server.core.neighbor.service.NeighborReplyService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/neighborhood")
class NeighborController(
    val neighborPostService: NeighborPostService,
    val neighborCommentService: NeighborCommentService,
    val neighborReplyService: NeighborReplyService
) {
    @Authenticated
    @GetMapping("")
    fun getAllNeighborPosts(
        @UserContext userId: Long,
        @RequestParam("keyword", required = false, defaultValue = "") neighborPostKeyword: String?,
        @RequestParam("page", required = false, defaultValue = "1") page: Int?
    ): NeighborPostPageResponse {
        val pageable = PageRequest.of(page!! - 1, 5)
        return neighborPostService.getAllNeighborPosts(userId, neighborPostKeyword!!, pageable)
    }

    @Authenticated
    @PostMapping("")
    fun createNeighborPost(
        @UserContext userId: Long,
        @Valid @RequestBody createNeighborPostRequest: CreateNeighborPostRequest
    ): NeighborPostResponse {
        return neighborPostService.createNeighborPost(userId, createNeighborPostRequest)
    }

    @Authenticated
    @GetMapping("/{post-id}")
    fun getNeighborPost(
        @PathVariable("post-id") postId: Long,
        @UserContext userId: Long
    ): NeighborPostResponse {
        return neighborPostService.getNeighborPost(userId, postId)
    }

    @Authenticated
    @PatchMapping("/{post-id}")
    fun updateNeighborPost(
        @UserContext userId: Long,
        @PathVariable("post-id") postId: Long,
        @Valid @RequestBody updateNeighborPostRequest: UpdateNeighborPostRequest
    ): NeighborPostResponse {
        return neighborPostService.updateNeighborPost(userId, postId, updateNeighborPostRequest)
    }

    @Authenticated
    @DeleteMapping("/{post-id}")
    fun deleteNeighborPost(
        @UserContext userId: Long,
        @PathVariable("post-id") postId: Long
    ) {
        return neighborPostService.deleteNeighborPost(userId, postId)
    }

    @Authenticated
    @PostMapping("/{post-id}/like")
    fun likeNeighborPost(
        @UserContext userId: Long,
        @PathVariable("post-id") postId: Long
    ): NeighborPostResponse {
        return neighborPostService.likeOrUnlikeNeighborPost(userId, postId)
    }

    @Authenticated
    @PostMapping("/{post-id}/comment")
    fun createNeighborComment(
        @UserContext userId: Long,
        @PathVariable("post-id") postId: Long,
        @Valid @RequestBody createNeighborCommentRequest: CreateNeighborCommentRequest
    ) {
        return neighborCommentService.createNeighborComment(userId, postId, createNeighborCommentRequest)
    }

    @Authenticated
    @PatchMapping("/comment/{comment-id}")
    fun updateNeighborComment(
        @UserContext userId: Long,
        @PathVariable("comment-id") commentId: Long,
        @Valid @RequestBody updateNeighborCommentRequest: UpdateNeighborCommentRequest
    ) {
        return neighborCommentService.updateNeighborComment(userId, commentId, updateNeighborCommentRequest)
    }

    @Authenticated
    @DeleteMapping("/comment/{comment-id}")
    fun deleteNeighborComment(
        @UserContext userId: Long,
        @PathVariable("comment-id") commentId: Long
    ) {
        return neighborCommentService.deleteNeighborComment(userId, commentId)
    }

    @Authenticated
    @PostMapping("/comment/{comment-id}/reply")
    fun createNeighborReply(
        @UserContext userId: Long,
        @PathVariable("comment-id") commentId: Long,
        @Valid @RequestBody createNeighborReplyRequest: CreateNeighborReplyRequest
    ) {
        return neighborReplyService.createNeighborReply(userId, commentId, createNeighborReplyRequest)
    }

    @Authenticated
    @PatchMapping("/comment/reply/{reply-id}")
    fun updateNeighborReply(
        @UserContext userId: Long,
        @PathVariable("reply-id") replyId: Long,
        @Valid @RequestBody updateNeighborReplyRequest: UpdateNeighborReplyRequest
    ) {
        return neighborReplyService.updateNeighborReply(userId, replyId, updateNeighborReplyRequest)
    }

    @Authenticated
    @DeleteMapping("/comment/reply/{reply-id}")
    fun deleteNeighborReply(
        @UserContext userId: Long,
        @PathVariable("reply-id") replyId: Long
    ) {
        return neighborReplyService.deleteNeighborReply(userId, replyId)
    }
}
