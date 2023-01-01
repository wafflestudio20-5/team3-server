package com.wafflestudio.team03server.core.user.api.vo

data class KakaoAuthToken(
    val access_token: String = "",
    val expires_in: Int = 0,
    val refresh_token: String = "",
    val refresh_token_expires_in: Int = 0,
    val scope: String = "",
    val token_type: String = "",
)
