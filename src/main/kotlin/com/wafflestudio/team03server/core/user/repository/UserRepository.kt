package com.wafflestudio.team03server.core.user.repository

import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByVerificationToken(verificationToken: String): User?
    fun findByUsername(username: String): User?
}
