package com.wafflestudio.team03server.core.trade.repository

import com.querydsl.core.QueryResults
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.trade.entity.QTradePost.*
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.user.entity.QUser.*
import org.springframework.data.domain.Pageable

class TradePostCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : TradePostCustomRepository {

    override fun findPostByIdWithSellerAndBuyer(postId: Long): TradePost? {
        return jpaQueryFactory
            .selectFrom(tradePost)
            .innerJoin(tradePost.seller).fetchJoin()
            .leftJoin(tradePost.buyer).fetchJoin()
            .where(tradePost.id.eq(postId))
            .fetchOne()
    }

    override fun findAllPostWithSellerAndBuyer(keyword: String?, pagable: Pageable): QueryResults<TradePost> {
        return jpaQueryFactory
            .selectFrom(tradePost)
            .where(eqKeyword(keyword))
            .innerJoin(tradePost.seller).fetchJoin()
            .leftJoin(tradePost.buyer).fetchJoin()
            .orderBy(tradePost.createdAt.desc())
            .offset(pagable.offset)
            .limit(pagable.pageSize.toLong())
            .fetchResults()
    }

    private fun eqKeyword(keyword: String?): BooleanExpression? {
        return keyword?.let { tradePost.title.contains(keyword) }
    }
}
