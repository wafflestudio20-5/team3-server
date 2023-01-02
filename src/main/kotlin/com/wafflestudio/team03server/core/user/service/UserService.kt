package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.*
import com.wafflestudio.team03server.core.user.api.request.EditPasswordRequest
import com.wafflestudio.team03server.core.user.api.request.EditProfileRequest
import com.wafflestudio.team03server.core.user.api.response.UserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun getProfile(userId: Long): UserResponse
    fun editProfile(userId: Long, editProfileRequest: EditProfileRequest): UserResponse
    fun editPassword(userId: Long, editPasswordRequest: EditPasswordRequest)
}

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val authService: AuthService,
    private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun getProfile(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        return UserResponse.of(user)
    }

    override fun editProfile(userId: Long, editProfileRequest: EditProfileRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        if (editProfileRequest.username != user.username && authService.isDuplicateUsername(editProfileRequest.username!!)) {
            throw Exception409("이미 존재하는 유저네임 입니다.")
        }
        user.username = editProfileRequest.username
        user.location = editProfileRequest.location!!
        user.imgUrl = editProfileRequest.imgUrl
        return UserResponse.of(user)
    }

    override fun editPassword(userId: Long, editPasswordRequest: EditPasswordRequest) {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        if (!passwordEncoder.matches(editPasswordRequest.password, user.password)) {
            throw Exception403("기존 비밀번호가 틀렸습니다.")
        }
        if (editPasswordRequest.newPassword != editPasswordRequest.newPasswordConfirm) {
            throw Exception400("비밀번호가 일치하지 않습니다.")
        }
        user.password = passwordEncoder.encode(editPasswordRequest.newPassword)
    }
}
