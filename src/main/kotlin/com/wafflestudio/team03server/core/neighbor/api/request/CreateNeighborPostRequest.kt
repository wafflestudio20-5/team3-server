package com.wafflestudio.team03server.core.neighbor.api.request

import javax.validation.constraints.NotBlank

data class CreateNeighborPostRequest(
    @field:NotBlank(message = "글 제목을 입력해주세요.")
    val title: String?,
    @field:NotBlank(message = "내용을 입력해주세요.")
    val content: String?
)
