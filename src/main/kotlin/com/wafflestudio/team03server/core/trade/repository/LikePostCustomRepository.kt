package com.wafflestudio.team03server.core.trade.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.trade.entity.LikePost
import com.wafflestudio.team03server.core.trade.entity.QLikePost.*
import com.wafflestudio.team03server.core.user.entity.User

interface LikePostCustomRepository {
    fun findLikePostsWithUserAndPostByUserId(user: User): List<LikePost>
}

class LikePostCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : LikePostCustomRepository {
    override fun findLikePostsWithUserAndPostByUserId(user: User): List<LikePost> {
        return jpaQueryFactory
            .selectFrom(likePost)
            .innerJoin(likePost.user).fetchJoin()
            .innerJoin(likePost.likedPost).fetchJoin()
            .where(likePost.user.eq(user))
            .fetch()
    }
}
