package com.wafflestudio.team03server.core.chat.api

import com.wafflestudio.team03server.common.Authenticated
import com.wafflestudio.team03server.common.UserContext
import com.wafflestudio.team03server.core.chat.api.response.ChatResponse
import com.wafflestudio.team03server.core.chat.dto.ChatMessage
import com.wafflestudio.team03server.core.chat.service.ChatService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    val messagingTemplate: SimpMessagingTemplate,
    val chatService: ChatService,
) {

    // "/pub/chat/message"로 오는 채팅을 처리
    @MessageMapping("/message")
    fun handleMessage(message: ChatMessage) {
        chatService.saveMessage(message)
        messagingTemplate.convertAndSend("/sub/room/${message.roomUUID}", message)
    }

    @Authenticated
    @GetMapping("/chat/room/{pid}")
    fun startChat(@UserContext userId: Long, @PathVariable(name = "pid") postId: Long): ChatResponse {
        return chatService.startChat(userId, postId)
    }
}
