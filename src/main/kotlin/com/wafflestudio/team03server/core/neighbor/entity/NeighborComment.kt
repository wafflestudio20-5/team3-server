package com.wafflestudio.team03server.core.neighbor.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.*

@Entity
class NeighborComment(
    neighborPost: NeighborPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_commenter_id")
    val commenter: User,

    var comment: String,

    @OneToMany(mappedBy = "neighborComment", cascade = [CascadeType.ALL])
    var replies: MutableList<NeighborReply> = mutableListOf()

) : BaseTimeEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_post_id")
    var neighborPost = neighborPost
        set(post) {
            field.comments.remove(this)
            field = post
            field.comments.add(this)
        }
}
