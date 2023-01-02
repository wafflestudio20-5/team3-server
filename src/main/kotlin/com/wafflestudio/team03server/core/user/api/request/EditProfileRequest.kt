package com.wafflestudio.team03server.core.user.api.request

import javax.validation.constraints.NotBlank

data class EditProfileRequest(
    @field:NotBlank(message = "유저네임은 필수 항목입니다.")
    val username:String?,
    @field:NotBlank(message = "주소는 필수 항목입니다.")
    val location:String?,
    @field:NotBlank(message = "프로필 이미지 주소는 필수 항목입니다.")
    val imgUrl:String?
)
