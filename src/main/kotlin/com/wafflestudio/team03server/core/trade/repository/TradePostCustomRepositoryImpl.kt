package com.wafflestudio.team03server.core.trade.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.trade.entity.QTradePost.*
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.QUser.*

class TradePostCustomRepositoryImpl(
    val jpaQueryFactory: JPAQueryFactory,
) : TradePostCustomRepository {

    // 다대일 조인
    // seller
    // buyer
    //
    override fun findPostByIdWithSellerAndBuyer(postId: Long): TradePost? {
        return jpaQueryFactory
            .selectFrom(tradePost)
            .join(tradePost.seller, user).fetchJoin()
            .join(tradePost.buyer, user).fetchJoin()
            .where(tradePost.id.eq(postId))
            .fetchOne()
    }
}
