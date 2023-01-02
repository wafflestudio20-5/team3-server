package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class EditPasswordRequest(
    val password:String,
    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,15}\$",
        message = "비밀번호는 특수문자, 영문자, 숫자 포함 8~15자 여야 합니다")
    val newPassword:String?,
    val newPasswordConfirm:String
)
