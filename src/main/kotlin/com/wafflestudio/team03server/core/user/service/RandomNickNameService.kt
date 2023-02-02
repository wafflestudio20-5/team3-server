package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.core.user.api.response.RandomNickNameResponse
import com.wafflestudio.team03server.core.user.api.vo.RandomNickNameVO
import com.wafflestudio.team03server.core.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RandomNickNameService(
    val userRepository: UserRepository,
) {
    fun getRandomNickname(): RandomNickNameResponse {
        val randomNickname = findRandomNickNameWhileNotDuplicated()
        return RandomNickNameResponse(randomNickname)
    }

    private fun findRandomNickNameWhileNotDuplicated(): String {
        var randomNickName = requestRandomNickname()
        while (userRepository.findByUsername(randomNickName) != null && isValid(randomNickName)) {
            randomNickName = requestRandomNickname()
        }
        return randomNickName
    }

    private fun isValid(randomNickName: String): Boolean {
        val regex = "^([a-zA-Z0-9가-힣]){2,10}\$".toRegex()
        return regex matches randomNickName
    }

    private fun requestRandomNickname(): String {
        val restTemplate = RestTemplate()
        val res = restTemplate.getForEntity(
            "https://nickname.hwanmoo.kr/?format=json&max_length=10",
            RandomNickNameVO::class.java
        )
        return res.body!!.words[0].filterNot { it.isWhitespace() }
    }
}
