package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank

data class LoginRequest(
    val email: String,
    val password: String
)
