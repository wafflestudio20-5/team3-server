package com.wafflestudio.team03server.core.neighbor.repository

import com.wafflestudio.team03server.core.neighbor.entity.NeighborLike
import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface NeighborLikeRepository : JpaRepository<NeighborLike, Long> {
    fun findNeighborLikeByLikedPostAndLiker(likedPost: NeighborPost, liker: User): NeighborLike?
    fun countAllByDeleteStatusEqualsAndLikedPost(deleteStatus: Boolean = false, likedPost: NeighborPost): Int
}
