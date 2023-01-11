package com.wafflestudio.team03server.core.user.api.response

import com.wafflestudio.team03server.core.user.entity.User

data class SimpleUserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val imgUrl: String?,
    val location: String,
    val temperature: Double,
) {
    companion object {
        fun of(user: User): SimpleUserResponse {
            return SimpleUserResponse(
                id = user.id,
                email = user.email,
                imgUrl = user.imgUrl,
                username = user.username,
                location = user.location,
                temperature = user.temperature,
            )
        }
    }
}
