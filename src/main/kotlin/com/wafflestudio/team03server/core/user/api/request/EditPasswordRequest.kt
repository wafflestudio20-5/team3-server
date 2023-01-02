package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank

data class EditPasswordRequest(
    val password:String,
    @field:NotBlank(message = "비밀번호는 필수 항목입니다.")
    val newPassword:String?,
    val newPasswordConfirm:String
)
