package com.wafflestudio.team03server.core.neighbor.api.request

import javax.validation.constraints.NotBlank

data class UpdateNeighborReplyRequest(
    @field: NotBlank(message = "내용을 입력해주세요.")
    val message: String?
)
