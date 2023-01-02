package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class EditProfileRequest(
    @field:Pattern(regexp = "^[\\p{L}\\p{Nd}가-힣]{1,10}\$",
        message = "유저네임은 영문자, 숫자, 한글 중 하나 이상을 포함해야 하며, 1~10자 여야 합니다.")
    val username:String?,
    @field:NotBlank(message = "주소는 필수 항목입니다.")
    val location:String?,
    @field:NotBlank(message = "프로필 이미지 주소는 필수 항목입니다.")
    val imgUrl:String?
)
