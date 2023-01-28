package com.wafflestudio.team03server.core.trade.repository

import com.querydsl.core.QueryResults
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.team03server.core.trade.entity.QTradePost.tradePost
import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import org.springframework.data.domain.Pageable

private const val TOP_RANK_CONST = 3L

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

    override fun findTopThreeLikePosts(): List<TradePost> {
        return jpaQueryFactory
            .selectFrom(tradePost)
            .orderBy(tradePost.likeTradePosts.size().desc())
            .innerJoin(tradePost.seller).fetchJoin()
            .leftJoin(tradePost.buyer).fetchJoin()
            .where(tradePost.tradeStatus.eq(TradeStatus.COMPLETED).not())
            .limit(TOP_RANK_CONST)
            .fetch()
    }

    private fun eqKeyword(keyword: String?): BooleanExpression? {
        return keyword?.let { tradePost.title.contains(keyword) }
    }
}
