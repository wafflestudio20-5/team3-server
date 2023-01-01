package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.user.api.response.UserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService{
    fun getUser(userId:Long):UserResponse
}

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository
):UserService{
    override fun getUser(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        return UserResponse.of(user)
    }
}
