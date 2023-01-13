package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class EditUsernameRequest(
    @field:Pattern(
        regexp = "^([a-zA-Z0-9가-힣]){2,10}\$",
        message = "유저네임은 영문자, 숫자, 한글 중 하나 이상을 포함해야 하며, 2~10자 여야 합니다."
    )
    @field:NotBlank(message = "유저네임은 필수 항목입니다.")
    val username: String?
)
