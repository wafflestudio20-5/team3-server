package com.wafflestudio.team03server.core.neighbor.repository

import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import org.springframework.data.jpa.repository.JpaRepository

interface NeighborPostRepository : JpaRepository<NeighborPost, Long>
