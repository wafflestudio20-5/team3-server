package com.wafflestudio.team03server.core.user.api.response

data class GoogleLoginResponse(
    val accessToken: String,
    val user: SimpleUserResponse,
)
