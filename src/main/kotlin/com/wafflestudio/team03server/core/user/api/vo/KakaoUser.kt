package com.wafflestudio.team03server.core.user.api.vo

data class KakaoUser(
    val connected_at: String = "",
    val id: Long = 0,
    val kakao_account: KakaoAccount = KakaoAccount(),
)
