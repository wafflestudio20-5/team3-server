package com.wafflestudio.team03server.core.neighbor.repository

import com.wafflestudio.team03server.core.neighbor.entity.NeighborPost
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface NeighborPostRepository : JpaRepository<NeighborPost, Long> {
    @Query(
        value = "SELECT SQL_CALC_FOUND_ROWS p.*, ST_Distance_Sphere(u.coordinate, :point) " +
            "as distance FROM neighbor_post p JOIN users u ON p.publisher_id = u.id WHERE p.content " +
            "LIKE :keyword HAVING distance <= :distance ORDER BY p.created_at DESC LIMIT :limit OFFSET :offset",
        nativeQuery = true
    )
    fun findByKeywordAndDistance(
        @Param("point") point: Point,
        @Param("keyword") keyword: String,
        @Param("distance") distance: Double,
        @Param("limit") limit: Int,
        @Param("offset") offset: Long,
    ): List<NeighborPost>

    @Query(value = "SELECT FOUND_ROWS()", nativeQuery = true)
    fun getTotalRecords(): Long

    @Query(
        value = "SELECT SQL_CALC_FOUND_ROWS p.* FROM neighbor_post p JOIN users u ON p.publisher_id = u.id " +
            "WHERE p.publisher_id = :publisher_id ORDER BY p.created_at  DESC Limit :limit OFFSET :offset",
        nativeQuery = true
    )
    fun findByPublisherId(
        @Param("limit") limit: Int,
        @Param("offset") offset: Long,
        @Param("publisher_id") publisher_id: Long
    ): List<NeighborPost>

    @Query(
        value = "SELECT SQL_CALC_FOUND_ROWS p.* FROM neighbor_post p JOIN users u ON p.publisher_id = u.id " +
            "LEFT JOIN neighbor_like l ON p.id = l.neighbor_post_id " +
            "WHERE l.liker_id = :liker_id ORDER BY p.created_at DESC Limit :limit OFFSET :offset",
        nativeQuery = true
    )
    fun findByLikerId(
        @Param("limit") limit: Int,
        @Param("offset") offset: Long,
        @Param("liker_id") liker_id: Long
    ): List<NeighborPost>
}
