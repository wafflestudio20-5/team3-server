package com.wafflestudio.team03server.core.trade.repository

import com.wafflestudio.team03server.core.trade.entity.TradePost
import org.springframework.data.jpa.repository.JpaRepository

interface TradePostRepository : JpaRepository<TradePost, Long>
