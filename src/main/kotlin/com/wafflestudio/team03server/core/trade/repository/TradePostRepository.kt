package com.wafflestudio.team03server.core.trade.repository

import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import com.wafflestudio.team03server.core.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface TradePostRepository : JpaRepository<TradePost, Long>, TradePostCustomRepository {
    fun findAllByBuyerAndTradeStatus(buyer: User, tradeStatus: TradeStatus): List<TradePost>
    fun findAllBySeller(seller: User): List<TradePost>
}
