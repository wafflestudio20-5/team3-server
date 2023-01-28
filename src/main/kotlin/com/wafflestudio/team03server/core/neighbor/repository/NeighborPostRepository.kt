package com.wafflestudio.team03server.core.neighbor.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import com.wafflestudio.team03server.core.neighbor.entity.QNeighborPost.neighborPost
import com.wafflestudio.team03server.core.user.entity.QUser.user
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface NeighborPostRepository : JpaRepository<NeighborPost, Long>, NeighborPostSupport

interface NeighborPostSupport {
    fun findAllByQuerydsl(pageable: Pageable): List<NeighborPost>
    fun findAllByContentContains(neighborPostKeyword: String, pageable: Pageable): List<NeighborPost>
}

@Component
class NeighborPostSupportImpl(
    private val queryFactory: JPAQueryFactory
) : NeighborPostSupport {
    override fun findAllByQuerydsl(pageable: Pageable): List<NeighborPost> {
        return queryFactory
            .selectFrom(neighborPost)
            .leftJoin(neighborPost.publisher, user)
            .fetchJoin()
            .orderBy(neighborPost.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .distinct()
            .fetch()
    }

    override fun findAllByContentContains(neighborPostKeyword: String, pageable: Pageable): List<NeighborPost> {
        return queryFactory
            .selectFrom(neighborPost)
            .where(neighborPost.content.contains(neighborPostKeyword))
            .leftJoin(neighborPost.publisher, user)
            .fetchJoin()
            .orderBy(neighborPost.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .distinct()
            .fetch()
    }
}
