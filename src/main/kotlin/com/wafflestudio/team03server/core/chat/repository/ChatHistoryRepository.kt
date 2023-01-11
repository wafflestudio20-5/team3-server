package com.wafflestudio.team03server.core.chat.repository

import com.wafflestudio.team03server.core.chat.entity.ChatHistory
import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ChatHistoryRepository : JpaRepository<ChatHistory, Long> {
    fun findChatHistoryBySender(sender: User): ChatHistory?
}
