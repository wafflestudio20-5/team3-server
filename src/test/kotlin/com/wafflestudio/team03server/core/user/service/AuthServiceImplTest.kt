package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.common.Exception409
import com.wafflestudio.team03server.core.user.api.request.SignUpRequest
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest
@Transactional
internal class AuthServiceImplTest @Autowired constructor(
    val authService: AuthService,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val authTokenService: AuthTokenService
) {

    @Test
    fun 회원가입_성공() {
        //given
        val signUpRequest = SignUpRequest("user1", "a@naver.com", "1234", "송도동")
        //when
        val loginResponse = authService.signUp(signUpRequest)
        //then
        val user = userRepository.findByIdOrNull(loginResponse.user.id) ?: throw Exception404("")
        assertThat(user.username).isEqualTo(signUpRequest.username)
        assertThat(user.email).isEqualTo(signUpRequest.email)
        assertThat(passwordEncoder.matches(signUpRequest.password, user.password)).isTrue
        assertThat(user.location).isEqualTo(signUpRequest.location)
    }

    @Test
    fun 회원가입_실패_중복_이메일() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        userRepository.save(user)
        val signUpRequest = SignUpRequest("user2", "a@naver.com", "1234", "송도동")
        //when
        val exception = assertThrows(Exception409::class.java) {
            authService.signUp(signUpRequest)
        }
        //then
        assertThat(exception.message).isEqualTo("이미 존재하는 이메일 입니다.")
    }

    @Test
    fun 회원가입_실패_중복_유저네임() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        userRepository.save(user)
        val signUpRequest = SignUpRequest("user1", "b@naver.com", "1234", "송도동")
        //when
        val exception = assertThrows(Exception409::class.java) {
            authService.signUp(signUpRequest)
        }
        //then
        assertThat(exception.message).isEqualTo("이미 존재하는 유저네임 입니다.")
    }

    @Test
    fun 이메일_중복체크_성공() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        userRepository.save(user)
        //when
        val result = authService.isDuplicateEmail(user.email)
        //then
        assertThat(result).isTrue
    }

    @Test
    fun 유저네임_중복체크_성공() {
        //given
        val user = User("user1", "a@naver.com", "1234", "송도동")
        userRepository.save(user)
        //when
        val result = authService.isDuplicateUsername(user.username)
        //then
        assertThat(result).isTrue
    }

    @Test
    fun 로그인_성공() {
        //given
        val signUpRequest = SignUpRequest("user1", "b@naver.com", "1234", "송도동")
        authService.signUp(signUpRequest)
        //when
        val loginResponse = authService.login(signUpRequest.email!!, signUpRequest.password!!)
        //then
        authTokenService.verifyToken(loginResponse.accessToken)
    }

    @Test
    fun 로그인_실패_이메일_틀림() {
        //given
        val signUpRequest = SignUpRequest("user1", "b@naver.com", "1234", "송도동")
        authService.signUp(signUpRequest)
        //when
        val exception = assertThrows(Exception403::class.java) {
            authService.login("wrong email", signUpRequest.password!!)
        }
        //then
        assertThat(exception.message).isEqualTo("이메일 또는 비밀번호가 잘못되었습니다.")
    }

    @Test
    fun 로그인_실패_비밀번호_틀림() {
        //given
        val signUpRequest = SignUpRequest("user1", "b@naver.com", "1234", "송도동")
        authService.signUp(signUpRequest)
        //when
        val exception = assertThrows(Exception403::class.java) {
            authService.login(signUpRequest.email!!, "wrong password")
        }
        //then
        assertThat(exception.message).isEqualTo("이메일 또는 비밀번호가 잘못되었습니다.")
    }
}
