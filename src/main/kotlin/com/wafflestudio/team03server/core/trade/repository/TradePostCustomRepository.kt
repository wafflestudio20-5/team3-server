package com.wafflestudio.team03server.core.trade.repository

import com.querydsl.core.QueryResults
import com.wafflestudio.team03server.core.trade.entity.TradePost
import org.springframework.data.domain.Pageable

interface TradePostCustomRepository {
    fun findPostByIdWithSellerAndBuyer(postId: Long): TradePost?
    fun findAllPostWithSellerAndBuyer(keyword: String?, pageable: Pageable): QueryResults<TradePost>
}
