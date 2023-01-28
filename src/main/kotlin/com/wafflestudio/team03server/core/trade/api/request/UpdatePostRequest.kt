package com.wafflestudio.team03server.core.trade.api.request

import javax.validation.constraints.PositiveOrZero

data class UpdatePostRequest(
    val title: String?,
    val desc: String?,
    @field:PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
    val price: Int?,
    val imgUrls: List<String>,
)
