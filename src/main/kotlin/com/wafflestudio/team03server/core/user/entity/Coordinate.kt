package com.wafflestudio.team03server.core.user.entity

import javax.persistence.Embeddable

@Embeddable
data class Coordinate(
    var lat: Double,
    var lng: Double
)
