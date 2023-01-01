package com.wafflestudio.team03server.core.user.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "users")
class User(
    @Column(unique = true)
    @NotNull
    val username: String,
    @Column(unique = true)
    @NotNull
    val email: String,
    @NotNull
    var password: String,
    @NotNull
    val location: String,
    @NotNull
    val temperature: Double = 36.5,
    val imgUrl: String? = null,
    @Column(unique = true)
    var verificationToken: String? = null,
    var emailVerified: Boolean = false,
) : BaseTimeEntity()
