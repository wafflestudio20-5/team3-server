package com.wafflestudio.team03server.core.trade.api.request

import javax.validation.constraints.NotNull

data class CreateReviewRequest(
    @field:NotNull(message = "점수를 입력해주세요.")
    val score: Double?,
    val content: String?
)
