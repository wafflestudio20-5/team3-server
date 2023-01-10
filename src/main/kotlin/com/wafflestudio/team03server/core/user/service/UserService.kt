package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.*
import com.wafflestudio.team03server.core.user.api.request.EditLocationRequest
import com.wafflestudio.team03server.core.user.api.request.EditPasswordRequest
import com.wafflestudio.team03server.core.user.api.request.EditUsernameRequest
import com.wafflestudio.team03server.core.user.api.response.UserResponse
import com.wafflestudio.team03server.core.user.repository.UserRepository
import com.wafflestudio.team03server.utils.S3Service
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

interface UserService {
    fun getProfile(userId: Long): UserResponse
    fun editUsername(userId: Long, editUsernameRequest: EditUsernameRequest): UserResponse
    fun editLocation(userId: Long, editLocationRequest: EditLocationRequest): UserResponse
    fun editPassword(userId: Long, editPasswordRequest: EditPasswordRequest)
    fun uploadImage(userId: Long, image: MultipartFile): String
}

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val authService: AuthService,
    private val passwordEncoder: PasswordEncoder,
    private val s3Service: S3Service
) : UserService {
    override fun getProfile(userId: Long): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        return UserResponse.of(user)
    }

    override fun editUsername(userId: Long, editUsernameRequest: EditUsernameRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        if (isModifiedAndDuplicateUsername(editUsernameRequest.username!!, user.username)) {
            throw Exception409("이미 존재하는 유저네임 입니다.")
        }
        user.username = editUsernameRequest.username
        return UserResponse.of(user)
    }

    private fun isModifiedAndDuplicateUsername(originalName: String, newName: String): Boolean {
        return originalName != newName && authService.isDuplicateUsername(newName)
    }

    override fun editLocation(userId: Long, editLocationRequest: EditLocationRequest): UserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        user.location = editLocationRequest.location!!
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

    override fun uploadImage(userId: Long, image: MultipartFile): String {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception404("사용자를 찾을 수 없습니다.")
        val imgUrl = s3Service.upload(image)
        user.imgUrl = imgUrl
        return imgUrl
    }
}
