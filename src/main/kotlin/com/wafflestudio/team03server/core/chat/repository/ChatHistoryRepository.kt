package com.wafflestudio.team03server.core.chat.repository

import com.wafflestudio.team03server.core.chat.entity.ChatHistory
import org.springframework.data.jpa.repository.JpaRepository

interface ChatHistoryRepository : JpaRepository<ChatHistory, Long>
