package com.wafflestudio.team03server.core.neighbor.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class NeighborLike(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id")
    val liker: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_post_id")
    var likedPost: NeighborPost,

    var deleteStatus: Boolean = false
) : BaseTimeEntity() {

    fun mapLikedPost(post: NeighborPost) {
        this.likedPost.likes.remove(this)
        this.likedPost = post
        this.likedPost.likes.add(this)
    }

    fun changeStatus() {
        this.deleteStatus = !this.deleteStatus
    }
}
