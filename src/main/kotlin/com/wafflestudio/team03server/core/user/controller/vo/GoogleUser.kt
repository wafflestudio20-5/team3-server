package com.wafflestudio.team03server.core.user.controller.vo

data class GoogleUser(
    val id: String,
    val email: String,
    val verified_email: Boolean,
    val name: String,
    val given_name: String,
    val family_name: String,
    val picture: String,
    val locale: String,
)
