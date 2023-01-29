package com.wafflestudio.team03server.utils

import org.springframework.stereotype.Component

@Component
class QueryUtil {
    fun getNativeQueryKeyword(keyword: String): String {
        return when (keyword) {
            "" -> "%"
            else -> "%$keyword%"
        }
    }
}
