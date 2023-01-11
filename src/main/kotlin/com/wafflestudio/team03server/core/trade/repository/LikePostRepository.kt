package com.wafflestudio.team03server.core.trade.repository

import com.wafflestudio.team03server.core.trade.entity.LikePost
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface LikePostRepository : JpaRepository<LikePost, Long> {
    fun findLikePostByUserAndLikedPost(user: User, likedPost: TradePost): LikePost?
}
