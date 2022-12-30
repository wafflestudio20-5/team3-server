package com.wafflestudio.team03server.core.user.api.response

data class GoogleLoginResponse(
    val jwtToken: String,
    val user: SimpleUserResponse
)
