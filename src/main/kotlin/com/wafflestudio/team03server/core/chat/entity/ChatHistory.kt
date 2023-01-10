package com.wafflestudio.team03server.core.chat.entity

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
    val createdAt: LocalDateTime
)
