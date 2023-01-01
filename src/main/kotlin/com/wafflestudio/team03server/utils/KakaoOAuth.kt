package com.wafflestudio.team03server.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.team03server.core.user.api.vo.KakaoUser
import com.wafflestudio.team03server.core.user.api.vo.KakaoAuthToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class KakaoOAuth {

    @Value("\${kakao.client.id}")
    val KAKAO_CLIENT_ID: String? = null

    @Value("\${kakao.redirect.uri}")
    val KAKAO_REDIRECT_URI: String? = null

    fun getKaKaoAccessToken(code: String): String {
        val accessTokenResponse: ResponseEntity<String> = requestKaKaoToken(code)
        var kakaoAuthToken: KakaoAuthToken = getKaKaoAccessTokenVO(accessTokenResponse)
        return kakaoAuthToken.access_token
    }

    private fun getKaKaoAccessTokenVO(accessTokenResponse: ResponseEntity<String>): KakaoAuthToken {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(accessTokenResponse.body, KakaoAuthToken::class.java)
    }

    private fun requestKaKaoToken(code: String): ResponseEntity<String> {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("grant_type", "authorization_code") //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", KAKAO_CLIENT_ID) //카카오 앱 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI)
        params.add("code", code) //인가 코드 요청시 받은 인가 코드값, 프론트에서 받아오는 그 코드

        val kakaoTokenRequest: HttpEntity<MultiValueMap<String, String>> = HttpEntity(params, headers)

        return restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String::class.java,
        )
    }

    fun getKakaoUserInfo(accessToken: String): KakaoUser {
        val accountInfoResponse: ResponseEntity<String> = requestKakaoUserInfo(accessToken)
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(accountInfoResponse.body, KakaoUser::class.java)
    }

    private fun requestKakaoUserInfo(accessToken: String): ResponseEntity<String> {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $accessToken")
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val accountInfoRequest = HttpEntity<MultiValueMap<String, String>>(headers)

        // POST 방식으로 API 서버에 요청 보내고, response 받아옴
        val accountInfoResponse: ResponseEntity<String> = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            accountInfoRequest,
            String::class.java,
        )
        return accountInfoResponse
    }
}
