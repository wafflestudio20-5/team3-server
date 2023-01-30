package com.wafflestudio.team03server.core.trade.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

data class CreatePostRequest(
    @field:NotBlank(message = "글 제목을 입력해주세요.")
    val title: String,
    @field:NotBlank(message = "글 설명을 입력해주세요.")
    val desc: String,
    @field:PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    val price: Int,
    @field:NotNull(message = "이미지를 한 개 이상 등록해야 합니다.")
    @field:Size(min = 1, max = 5, message = "이미지는 1개 이상 5개 이하만 등록 가능합니다.")
    val imgUrls: List<String>?,
)
