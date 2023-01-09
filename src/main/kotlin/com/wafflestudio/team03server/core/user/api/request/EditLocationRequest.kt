package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank

data class EditLocationRequest(
    @field:NotBlank(message = "주소는 필수 항목입니다.")
    val location: String?
)
