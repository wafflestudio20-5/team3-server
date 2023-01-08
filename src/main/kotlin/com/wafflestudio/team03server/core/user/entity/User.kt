package com.wafflestudio.team03server.core.user.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.Reservation
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
    @NotNull
    val temperature: Double = 36.5,
    var imgUrl: String? = null,
    @Column(unique = true)
    var verificationToken: String? = null,
    var emailVerified: Boolean = false,

    @OneToMany(mappedBy = "seller")
    var sellPosts: MutableList<TradePost> = mutableListOf(),

    @OneToMany(mappedBy = "buyer")
    var buyPosts: MutableList<TradePost> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var reservations: MutableList<Reservation> = mutableListOf(),

) : BaseTimeEntity()
