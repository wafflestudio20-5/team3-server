package com.wafflestudio.team03server.core.user.api.request

import com.wafflestudio.team03server.core.user.entity.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank(message = "username is required")
    val username: String?,
    @field:Email(message = "Invalid email")
    @field:NotBlank(message = "email is required")
    val email: String?,
    @field:NotBlank(message = "password is required")
    val password: String?,
    @field:NotBlank(message = "location is required")
    val location: String?,
    val imageUrl: String? = null
) {
    fun toUser(): User {
        return User(username!!, email!!, password!!, location!!, imageUrl = imageUrl)
    }
}
