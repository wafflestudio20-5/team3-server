package com.wafflestudio.team03server.core.chat.api.response

import com.wafflestudio.team03server.core.chat.entity.ChatHistory
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import java.time.LocalDateTime

data class ChatResponse(
    val roomUUID: String,
    val chatHistories: List<ChatHistoryResponse> = mutableListOf()
) {
    companion object {
        fun of(chatRoom: ChatRoom): ChatResponse {
            val histories = chatRoom.histories.map { ChatHistoryResponse.of(it) }
            return ChatResponse(chatRoom.roomUUID, histories)
        }
    }
}

data class ChatHistoryResponse(
    val sender: String,
    val message: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(chatHistory: ChatHistory): ChatHistoryResponse {
            return ChatHistoryResponse(
                sender = chatHistory.sender.username,
                message = chatHistory.message,
                createdAt = chatHistory.createdAt!!,
            )
        }
    }
}
