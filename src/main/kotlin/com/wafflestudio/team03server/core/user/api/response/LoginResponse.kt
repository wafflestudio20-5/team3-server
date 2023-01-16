package com.wafflestudio.team03server.core.user.api.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: SimpleUserResponse
)
