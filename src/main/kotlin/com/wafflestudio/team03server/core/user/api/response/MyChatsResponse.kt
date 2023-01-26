package com.wafflestudio.team03server.core.user.api.response

import com.wafflestudio.team03server.core.chat.api.response.ChatHistoryResponse
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.trade.api.response.PostResponse
import com.wafflestudio.team03server.core.user.entity.User

data class MyChatsResponse(
    val chats: List<DetailChatResponse>
) {
    companion object {
        fun of(me: User, chatRooms: List<ChatRoom>): MyChatsResponse {
            return MyChatsResponse(
                chats = chatRooms.map { DetailChatResponse.of(me, it) },
            )
        }
    }
}

data class DetailChatResponse(
    val roomUUID: String,
    val you: SimpleUserResponse,
    val post: PostResponse,
    val lastChat: ChatHistoryResponse?,
) {
    companion object {
        fun of(me: User, chatRoom: ChatRoom): DetailChatResponse {
            return DetailChatResponse(
                roomUUID = chatRoom.roomUUID,
                you = SimpleUserResponse.of(getYou(chatRoom, me)),
                post = PostResponse.of(chatRoom.post, me),
                // OneToMany 관계인데 그 중 하나만 가져오고싶지만.. 다 가져온뒤 하나 선택
                lastChat = getLastChatResponseOrNull(chatRoom),
            )
        }

        private fun getYou(chatRoom: ChatRoom, me: User): User {
            return if (chatRoom.buyer == me) chatRoom.seller else chatRoom.buyer
        }

        private fun getLastChatResponseOrNull(chatRoom: ChatRoom): ChatHistoryResponse? {
            return if (chatRoom.histories.size == 0) null
            else ChatHistoryResponse.of(chatRoom.histories[chatRoom.histories.size - 1])
        }
    }
}
