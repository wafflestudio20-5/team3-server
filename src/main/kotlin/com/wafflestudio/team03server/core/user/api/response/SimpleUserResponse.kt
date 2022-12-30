package com.wafflestudio.team03server.core.user.api.response

import com.wafflestudio.team03server.core.user.entity.User

data class SimpleUserResponse(
    val email: String,
    val username: String,
    val location: String,
) {
    companion object {
        fun of(user: User): SimpleUserResponse {
            return SimpleUserResponse(
                email = user.email,
                username = user.username,
                location = user.location,
            )
        }
    }
}
