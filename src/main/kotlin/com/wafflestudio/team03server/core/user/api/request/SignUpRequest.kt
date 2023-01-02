package com.wafflestudio.team03server.core.user.api.request

import com.wafflestudio.team03server.core.user.entity.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank(message = "유저네임은 필수 항목입니다.")
    val username: String?,
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:NotBlank(message = "이메일은 필수 항목입니다.")
    val email: String?,
    @field:NotBlank(message = "비밀번호는 필수 항목입니다.")
    val password: String?,
    @field:NotBlank(message = "주소는 필수 항목입니다.")
    val location: String?,
    val imgUrl: String? = null,
) {
    fun toUser(): User {
        return User(username!!, email!!, password!!, location!!, imgUrl = imgUrl)
    }
}
