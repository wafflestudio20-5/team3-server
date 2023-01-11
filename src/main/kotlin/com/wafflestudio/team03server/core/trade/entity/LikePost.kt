package com.wafflestudio.team03server.core.trade.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class LikePost(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val likedPost: TradePost,

) : BaseTimeEntity() {
    fun removeLike() {
        this.user.likeTradePosts.remove(this)
        this.likedPost.likeTradePosts.remove(this)
    }

    fun addLike() {
        this.user.likeTradePosts.add(this)
        this.likedPost.likeTradePosts.add(this)
    }
}
