package com.wafflestudio.team03server.core.neighbor.repository

import com.wafflestudio.team03server.core.neighbor.entity.NeighborLike
import org.springframework.data.jpa.repository.JpaRepository

interface NeighborLikeRepository : JpaRepository<NeighborLike, Long>
