package com.wafflestudio.team03server.core.chat.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.*

@Entity
class ChatHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    val chatRoom: ChatRoom,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    val sender: User,

    val message: String,

) : BaseTimeEntity()
