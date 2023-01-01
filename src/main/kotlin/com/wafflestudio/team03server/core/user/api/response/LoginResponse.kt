package com.wafflestudio.team03server.core.user.api.response

data class LoginResponse(
    val jwtToken: String,
    val user: SimpleUserResponse,
)
