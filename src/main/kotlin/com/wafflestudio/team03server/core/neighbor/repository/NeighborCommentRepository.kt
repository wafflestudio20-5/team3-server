package com.wafflestudio.team03server.core.neighbor.repository

import com.wafflestudio.team03server.core.neighbor.entity.NeighborComment
import org.springframework.data.jpa.repository.JpaRepository

interface NeighborCommentRepository : JpaRepository<NeighborComment, Long>
