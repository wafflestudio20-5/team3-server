package com.wafflestudio.team03server.core.user.api.vo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAccount(
    val has_email: Boolean = false,
    val email_needs_agreement: Boolean = false,
    val is_email_valid: Boolean = false,
    val is_email_verified: Boolean = false,
    val email: String = "",
)
