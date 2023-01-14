package com.wafflestudio.team03server.core.chat.api.response

import com.wafflestudio.team03server.core.chat.entity.ChatHistory
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.user.api.response.SimpleUserResponse
import com.wafflestudio.team03server.core.user.entity.User
import java.time.LocalDateTime

data class MessagesResponse(
    val you: SimpleUserResponse,
    val chatHistories: List<ChatHistoryResponse> = mutableListOf()
) {

    companion object {
        fun of(you: User, chatRoom: ChatRoom): MessagesResponse {
            return MessagesResponse(
                you = SimpleUserResponse.of(you),
                chatHistories = chatRoom.histories.map { ChatHistoryResponse.of(it) }
            )
        }
    }
}

data class ChatHistoryResponse(
    val senderId: Long,
    val message: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(chatHistory: ChatHistory): ChatHistoryResponse {
            return ChatHistoryResponse(
                senderId = chatHistory.sender.id,
                message = chatHistory.message,
                createdAt = chatHistory.createdAt,
            )
        }
    }
}
