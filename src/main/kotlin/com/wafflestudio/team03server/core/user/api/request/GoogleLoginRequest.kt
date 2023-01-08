package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.Email

data class GoogleLoginRequest(
    @Email
    val email: String,
)
