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
    val email: String,
    @NotNull
    val location: String,
    @NotNull
    val password:String,
    @NotNull
    val temperature: Double,
    @NotNull
    val username: String,
    val imageUrl:String
):BaseTimeEntity() {
}