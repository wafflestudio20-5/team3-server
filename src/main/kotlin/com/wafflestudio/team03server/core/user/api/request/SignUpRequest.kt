package com.wafflestudio.team03server.core.user.api.request

import com.wafflestudio.team03server.core.user.entity.Coordinate
import com.wafflestudio.team03server.core.user.entity.User
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class SignUpRequest(
    @field:Pattern(
        regexp = "^([a-zA-Z0-9가-힣]){2,10}\$",
        message = "유저네임은 영문자, 숫자, 한글 중 하나 이상을 포함해야 하며, 2~10자 여야 합니다."
    )
    @field:NotBlank(message = "유저네임은 필수 항목입니다.")
    val username: String?,
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:NotBlank(message = "이메일은 필수 항목입니다.")
    val email: String?,
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,20}\$",
        message = "비밀번호는 특수문자, 영문자, 숫자 포함 8~20자 여야 합니다"
    )
    @field:NotBlank(message = "비밀번호는 필수 항목입니다.")
    val password: String?,
    @field:NotBlank(message = "주소는 필수 항목입니다.")
    val location: String?,
    @field:NotNull(message = "좌표 정보가 없습니다.")
    val coordinate: Coordinate?,
    @field:NotNull(message = "이메일 인증 정보가 없습니다.")
    val isEmailAuthed: Boolean?
) {
    fun toUser(): User {
        return User(username!!, email!!, password!!, location!!, coordinate!!)
    }
}
