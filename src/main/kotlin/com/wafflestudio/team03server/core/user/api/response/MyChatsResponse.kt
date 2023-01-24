package com.wafflestudio.team03server.core.user.api.response

import com.wafflestudio.team03server.core.chat.api.response.ChatHistoryResponse
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.trade.api.response.PostResponse

data class MyChatsResponse(
    val chats: List<DetailChatResponse>
) {
    companion object {
        fun of(chatRooms: List<ChatRoom>): MyChatsResponse {
            return MyChatsResponse(
                chats = chatRooms.map { DetailChatResponse.of(it) },
            )
        }
    }
}

data class DetailChatResponse(
    val roomUUID: String,
    val buyer: SimpleUserResponse,
    val post: PostResponse,
    val lastChat: ChatHistoryResponse,
) {
    companion object {
        fun of(chatRoom: ChatRoom): DetailChatResponse {
            return DetailChatResponse(
                roomUUID = chatRoom.roomUUID,
                buyer = SimpleUserResponse.of(chatRoom.buyer),
                post = PostResponse.of(chatRoom.post, chatRoom.seller),
                // OneToMany 관계인데 그 중 하나만 가져오고싶지만.. 다 가져온뒤 하나 선택
                lastChat = ChatHistoryResponse.of(chatRoom.histories[chatRoom.histories.size - 1]),
            )
        }
    }
}
