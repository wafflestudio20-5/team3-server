package com.wafflestudio.team03server.core.chat.dto

data class ChatMessage(
    val roomUUID: String,
    val senderId: Long,
    val message: String,
)
