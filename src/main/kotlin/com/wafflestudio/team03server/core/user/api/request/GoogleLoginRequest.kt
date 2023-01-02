package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class GoogleLoginRequest(
    val email: String,
)
