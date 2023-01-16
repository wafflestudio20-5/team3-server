package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank

data class RefreshRequest(
    @field:NotBlank(message = "리프레쉬 토큰이 필요합니다.")
    val refreshToken: String?
)
