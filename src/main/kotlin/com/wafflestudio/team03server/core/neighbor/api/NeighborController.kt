package com.wafflestudio.team03server.core.neighbor.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.neighbor.api.request.*
import com.wafflestudio.team03server.core.neighbor.api.response.NeighborPostResponse
import com.wafflestudio.team03server.core.neighbor.service.NeighborCommentService
import com.wafflestudio.team03server.core.neighbor.service.NeighborPostService
import com.wafflestudio.team03server.core.neighbor.service.NeighborReplyService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/neighborhood")
class NeighborController(
    val neighborPostService: NeighborPostService,
    val neighborCommentService: NeighborCommentService,
    val neighborReplyService: NeighborReplyService
) {
    @GetMapping("")
    fun getAllNeighborPosts(): List<NeighborPostResponse> {
        return neighborPostService.getAllNeighborPosts()
    }

    @Authenticated
    @PostMapping("")
    fun createNeighborPost(
        @UserContext userId: Long,
        @Valid @RequestBody createNeighborPostRequest: CreateNeighborPostRequest
    ): NeighborPostResponse {
        return neighborPostService.createNeighborPost(userId, createNeighborPostRequest)
    }

    @GetMapping("/{postId}")
    fun getNeighborPost(@PathVariable("postId") postId: Long): NeighborPostResponse {
        return neighborPostService.getNeighborPost(postId)
    }

    @Authenticated
    @PatchMapping("/{postId}")
    fun updateNeighborPost(
        @UserContext userId: Long,
        @PathVariable("postId") postId: Long,
        @Valid @RequestBody updateNeighborPostRequest: UpdateNeighborPostRequest
    ): NeighborPostResponse {
        return neighborPostService.updateNeighborPost(userId, postId, updateNeighborPostRequest)
    }

    @Authenticated
    @DeleteMapping("/{postId}")
    fun deleteNeighborPost(
        @UserContext userId: Long,
        @PathVariable("postId") postId: Long
    ) {
        return neighborPostService.deleteNeighborPost(userId, postId)
    }

    @Authenticated
    @PostMapping("/{postId}/like")
    fun likeNeighborPost(
        @UserContext userId: Long,
        @PathVariable("postId") postId: Long
    ) {
        return neighborPostService.likeNeighborPost(userId, postId)
    }

    @Authenticated
    @PostMapping("/{postId}/comment")
    fun createNeighborComment(
        @UserContext userId: Long,
        @PathVariable("postId") postId: Long,
        @Valid @RequestBody createNeighborCommentRequest: CreateNeighborCommentRequest
    ) {
        return neighborCommentService.createNeighborComment(userId, postId, createNeighborCommentRequest)
    }

    @Authenticated
    @PatchMapping("/comment/{commentId}")
    fun updateNeighborComment(
        @UserContext userId: Long,
        @PathVariable("commentId") commentId: Long,
        @Valid @RequestBody updateNeighborCommentRequest: UpdateNeighborCommentRequest
    ) {
        return neighborCommentService.updateNeighborComment(userId, commentId, updateNeighborCommentRequest)
    }

    @Authenticated
    @DeleteMapping("/comment/{commentId}")
    fun deleteNeighborComment(
        @UserContext userId: Long,
        @PathVariable("commentId") commentId: Long
    ) {
        return neighborCommentService.deleteNeighborComment(userId, commentId)
    }

    @Authenticated
    @PostMapping("/comment/{commentId}/reply")
    fun createNeighborReply(
        @UserContext userId: Long,
        @PathVariable("commentId") commentId: Long,
        @Valid @RequestBody createNeighborReplyRequest: CreateNeighborReplyRequest
    ) {
        return neighborReplyService.createNeighborReply(userId, commentId, createNeighborReplyRequest)
    }

    @Authenticated
    @PatchMapping("/comment/reply/{replyId}")
    fun updateNeighborReply(
        @UserContext userId: Long,
        @PathVariable("replyId") replyId: Long,
        @Valid @RequestBody updateNeighborReplyRequest: UpdateNeighborReplyRequest
    ) {
        return neighborReplyService.updateNeighborReply(userId, replyId, updateNeighborReplyRequest)
    }

    @Authenticated
    @DeleteMapping("/comment/reply/{replyId}")
    fun deleteNeighborReply(
        @UserContext userId: Long,
        @PathVariable("replyId") replyId: Long
    ) {
        return neighborReplyService.deleteNeighborReply(userId, replyId)
    }
}
