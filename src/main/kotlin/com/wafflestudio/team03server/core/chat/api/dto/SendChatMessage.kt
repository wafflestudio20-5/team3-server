package com.wafflestudio.team03server.core.chat.api.dto

import java.time.LocalDateTime

data class SendChatMessage(
    val chatId: Long,
    val roomUUID: String,
    val senderId: Long,
    val message: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(chatId: Long, message: ReceivedChatMessage): SendChatMessage {
            return SendChatMessage(
                chatId = chatId,
                roomUUID = message.roomUUID,
                senderId = message.senderId,
                message = message.message,
                createdAt = message.createdAt,
            )
        }
    }
}
