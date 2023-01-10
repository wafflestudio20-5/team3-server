package com.wafflestudio.team03server.core.neighbor.entity

import com.wafflestudio.team03server.common.BaseTimeEntity
import com.wafflestudio.team03server.core.user.entity.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class NeighborComment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_post_id")
    var neighborPost: NeighborPost,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighbor_commenter_id")
    var commenter: User,

    var comment: String
) : BaseTimeEntity()
