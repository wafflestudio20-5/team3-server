package com.wafflestudio.team03server.core.neighbor.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import org.hibernate.annotations.BatchSize
import javax.persistence.*

@Entity
class NeighborComment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_post_id")
    var neighborPost: NeighborPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_commenter_id")
    val commenter: User,

    var comment: String,

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "neighborComment", cascade = [CascadeType.ALL])
    var replies: MutableList<NeighborReply> = mutableListOf()

) : BaseTimeEntity() {
    fun mapNeighborPost(post: NeighborPost) {
        this.neighborPost.comments.remove(this)
        this.neighborPost = post
        this.neighborPost.comments.add(this)
    }
}
