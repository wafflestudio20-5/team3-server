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
    val likedPost: NeighborPost,

    var deleteStatus: Boolean = false
) : BaseTimeEntity() {
    fun changeStatus() {
        this.deleteStatus = !this.deleteStatus
    }
}
