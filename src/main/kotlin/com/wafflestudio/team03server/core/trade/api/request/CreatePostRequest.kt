package com.wafflestudio.team03server.core.trade.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

data class CreatePostRequest(
    @field:NotBlank(message = "글 제목을 입력해주세요.")
    val title: String,
    @field:NotBlank(message = "글 설명을 입력해주세요.")
    val desc: String,
    @field:PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    val price: Int,
)
