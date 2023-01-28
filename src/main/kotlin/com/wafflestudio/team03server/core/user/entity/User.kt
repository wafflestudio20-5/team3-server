package com.wafflestudio.team03server.core.user.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.chat.entity.ChatRoom
import com.wafflestudio.team03server.core.trade.entity.LikePost
import com.wafflestudio.team03server.core.trade.entity.TradePost
import javax.persistence.CascadeType
import com.wafflestudio.team03server.core.trade.entity.Review
import org.locationtech.jts.geom.Point
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "users")
class User(
    @Column(unique = true)
    @NotNull
    var username: String,
    @Column(unique = true)
    @NotNull
    val email: String,
    @NotNull
    var password: String,
    @NotNull
    var location: String,

    var coordinate: Point,

    @NotNull
    var temperature: Double = 36.5,
    var imgUrl: String? = null,

    @OneToMany(mappedBy = "seller")
    var sellPosts: MutableList<TradePost> = mutableListOf(),

    @OneToMany(mappedBy = "buyer")
    var buyPosts: MutableList<TradePost> = mutableListOf(),

    @OneToMany(mappedBy = "seller")
    val sellChatRooms: MutableList<ChatRoom> = mutableListOf(),

    @OneToMany(mappedBy = "buyer")
    val buyChatRooms: MutableList<ChatRoom> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val likeTradePosts: MutableList<LikePost> = mutableListOf(),

    @OneToMany(mappedBy = "reviewer")
    var reviewsIWrote: MutableList<Review> = mutableListOf(),

    @OneToMany(mappedBy = "reviewee")
    var reviewsIGot: MutableList<Review> = mutableListOf(),

    var refreshToken: String? = null

) : BaseTimeEntity()
