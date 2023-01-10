package com.wafflestudio.team03server.core.neighbor.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class NeighborReply(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_comment_id")
    var neighborComment: NeighborComment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_replyer_id")
    val replier: User,

    var replyingMessage: String
) : BaseTimeEntity() {
    fun mapNeighborComment(comment: NeighborComment) {
        this.neighborComment.replies.remove(this)
        this.neighborComment = comment
        this.neighborComment.replies.add(this)
    }
}
