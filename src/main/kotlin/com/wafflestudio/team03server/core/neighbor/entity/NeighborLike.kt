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

    likedPost: NeighborPost,

    var deleteStatus: Boolean = false
) : BaseTimeEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_post_id")
    var likedPost = likedPost
        set(post) {
            field.likes.remove(this)
            field = post
            field.likes.add(this)
        }

    fun changeStatus() {
        this.deleteStatus = !this.deleteStatus
    }
}
