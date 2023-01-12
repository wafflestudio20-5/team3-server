package com.wafflestudio.team03server.core.neighbor.repository

import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface NeighborPostRepository : JpaRepository<NeighborPost, Long> {
    fun findAllByTitleContains(neighborPostName: String, pageable: Pageable): Page<NeighborPost>
}
