package com.wafflestudio.team03server.core.user.controller.response

data class GoogleLoginResponse(
    val jwtToken: String,
    val userNumber: Int,
    val accessToken: String,
    val tokenType: String,
)
