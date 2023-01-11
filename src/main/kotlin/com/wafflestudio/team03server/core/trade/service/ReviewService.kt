package com.wafflestudio.team03server.core.trade.service

import org.springframework.stereotype.Service


interface ReviewService {
    fun createReview()
}

@Service
class ReviewServiceImpl : ReviewService {
    override fun createReview() {
        TODO("Not yet implemented")
    }
}
