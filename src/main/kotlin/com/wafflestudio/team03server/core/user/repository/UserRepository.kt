package com.wafflestudio.team03server.core.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.user.entity.QUser.user
import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface UserRepository : JpaRepository<User, Long>, UserSupport {
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
}

private const val TOP_RANK_CONST = 3L

interface UserSupport {
    fun findTopThreeWarmestPeople(): List<User>
}

@Component
class UserSupportImpl(
    private val queryFactory: JPAQueryFactory
) : UserSupport {
    override fun findTopThreeWarmestPeople(): List<User> {
        return queryFactory
            .selectFrom(user)
            .orderBy(user.temperature.desc())
            .limit(TOP_RANK_CONST)
            .fetch()
    }
}
