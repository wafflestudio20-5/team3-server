package com.wafflestudio.team03server.core.neighbor.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.*

@Entity
class NeighborPost(
    var title: String,
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    val publisher: User,

    @OneToMany(mappedBy = "neighborPost")
    var comments: MutableList<NeighborComment> = mutableListOf(),

    var viewCount: Int = 0
) : BaseTimeEntity()
