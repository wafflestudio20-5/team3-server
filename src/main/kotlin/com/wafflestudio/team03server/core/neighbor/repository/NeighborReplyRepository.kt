package com.wafflestudio.team03server.core.neighbor.repository

import com.wafflestudio.team03server.core.neighbor.entity.NeighborReply
import org.springframework.data.jpa.repository.JpaRepository

interface NeighborReplyRepository : JpaRepository<NeighborReply, Long>
