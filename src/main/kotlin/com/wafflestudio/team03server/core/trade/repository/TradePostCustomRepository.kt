package com.wafflestudio.team03server.core.trade.repository

import com.wafflestudio.team03server.core.trade.entity.TradePost

interface TradePostCustomRepository {
    fun findPostByIdWithSellerAndBuyer(postId: Long): TradePost?
}
