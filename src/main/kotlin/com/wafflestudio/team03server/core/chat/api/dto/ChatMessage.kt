package com.wafflestudio.team03server.core.chat.api.dto

import java.time.LocalDateTime

data class ChatMessage(
    val roomUUID: String,
    val senderId: Long,
    val message: String,
    val createdAt: LocalDateTime,
)
