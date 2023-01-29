package com.wafflestudio.team03server.core.user.entity

enum class SearchScope(val distance: Double) {
    NARROW(35000.0), NORMAL(200000.0), WIDE(400000.0)
}
