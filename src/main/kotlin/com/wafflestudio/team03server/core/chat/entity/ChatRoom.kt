package com.wafflestudio.team03server.core.chat.entity

import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.User
import java.util.*
import javax.persistence.*

@Entity
class ChatRoom(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "room_uuid")
    val roomUUID: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    val seller: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    val buyer: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: TradePost,

    @OneToMany(mappedBy = "chatRoom")
    @OrderBy("name ASC")
    val histories: MutableList<ChatHistory> = mutableListOf()

) {
    companion object {
        fun create(_seller: User, _buyer: User, _post: TradePost): ChatRoom {
            val chatRoom = ChatRoom(
                roomUUID = UUID.randomUUID().toString(),
                seller = _seller,
                buyer = _buyer,
                post = _post,
            )
            _buyer.buyChatRooms.add(chatRoom)
            _seller.sellChatRooms.add(chatRoom)
            _post.chatRooms.add(chatRoom)
            return chatRoom
        }
    }
}
