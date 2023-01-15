package com.wafflestudio.team03server.core.chat.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ChatMessage(
    val roomUUID: String,
    val senderId: Long,
    val message: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
)
