package com.wafflestudio.team03server.core.trade.repository

import com.wafflestudio.team03server.core.trade.entity.TradePost
import com.wafflestudio.team03server.core.trade.entity.TradeStatus
import com.wafflestudio.team03server.core.user.entity.User
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TradePostRepository : JpaRepository<TradePost, Long>, TradePostCustomRepository {
    fun findAllByBuyerAndTradeStatus(buyer: User, tradeStatus: TradeStatus): List<TradePost>
    fun findAllBySeller(seller: User): List<TradePost>

    @Query(
        value = "SELECT SQL_CALC_FOUND_ROWS p.*, ST_Distance_Sphere(u.coordinate, :point) as distance " +
            "FROM trade_post p JOIN users u ON p.seller_id = u.id WHERE p.title LIKE :keyword " +
            "HAVING distance <= :distance ORDER BY p.created_at DESC LIMIT :limit OFFSET :offset",
        nativeQuery = true
    )
    fun findByKeywordAndDistance(
        @Param("point") point: Point,
        @Param("keyword") keyword: String,
        @Param("distance") distance: Double,
        @Param("limit") limit: Int,
        @Param("offset") offset: Long,
    ): List<TradePost>

    @Query(value = "SELECT FOUND_ROWS()", nativeQuery = true)
    fun getTotalRecords(): Long
}
