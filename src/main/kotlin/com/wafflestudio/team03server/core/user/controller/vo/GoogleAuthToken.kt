package com.wafflestudio.team03server.core.user.controller.vo

data class GoogleAuthToken(
    val access_token: String,
    val scope: String,
    val token_type: String,
    val id_token: String,
    val expires_in: Int,
)
