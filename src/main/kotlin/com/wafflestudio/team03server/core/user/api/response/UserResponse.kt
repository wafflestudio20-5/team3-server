package com.wafflestudio.team03server.core.user.api.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.wafflestudio.team03server.core.user.entity.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val location: String,
    val temperature: Double,
    val imgUrl: String?,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
) {
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                id = user.id,
                username = user.username,
                email = user.email,
                location = user.location,
                temperature = user.temperature,
                imgUrl = user.imgUrl,
                createdAt = user.createdAt!!,
                modifiedAt = user.modifiedAt!!
            )
        }
    }
}
