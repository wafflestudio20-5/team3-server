package com.wafflestudio.team03server.core.user.api.request

data class LoginRequest(
    val email: String,
    val password: String
)
