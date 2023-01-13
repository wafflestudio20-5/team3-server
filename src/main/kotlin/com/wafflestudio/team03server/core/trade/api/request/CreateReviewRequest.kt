package com.wafflestudio.team03server.core.trade.api.request

data class CreateReviewRequest(
    val score: Double,
    val content: String?
)
