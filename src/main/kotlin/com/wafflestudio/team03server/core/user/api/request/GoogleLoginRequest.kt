package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class GoogleLoginRequest(
    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "email is required")
    val email: String,
)
