package com.wafflestudio.team03server.core.chat.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.wafflestudio.team03server.core.user.entity.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ChatHistory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    val sender: User,

    val message: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime
) {
    companion object {
        fun create(_chatRoom: ChatRoom, _sender: User, _message: String, _createdAt: LocalDateTime): ChatHistory {
            val chatHistory = ChatHistory(
                chatRoom = _chatRoom,
                sender = _sender,
                message = _message,
                createdAt = _createdAt,
            )
            _chatRoom.histories.add(chatHistory)
            return chatHistory
        }
    }
}
