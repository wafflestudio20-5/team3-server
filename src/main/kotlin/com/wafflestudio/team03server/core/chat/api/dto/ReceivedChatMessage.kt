package com.wafflestudio.team03server.core.chat.api.dto

import java.time.LocalDateTime

data class ReceivedChatMessage(
    val roomUUID: String,
    val senderId: Long,
    val message: String,
    val createdAt: LocalDateTime,
)
