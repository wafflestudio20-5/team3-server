package com.wafflestudio.team03server.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.team03server.core.user.controller.vo.GoogleAuthToken
import com.wafflestudio.team03server.core.user.controller.vo.GoogleUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class GoogleOAuth(
    private val objectMapper: ObjectMapper
) {
    @Value("\${google.url}")
    private val GOOGLE_LOGIN_URL: String? = null

    @Value("\${google.client.id}")
    private val GOOGLE_CLIENT_ID: String? = null

    @Value("\${google.callback-url}")
    private val GOOGLE_CALLBACK_URL: String? = null

    @Value("\${google.secret}")
    private val GOOGLE_CLIENT_SECRET: String? = null

    @Value("\${google.scope}")
    private val GOOGLE_SCOPE: String? = null

    private val GOOGLE_TOKEN_REQUEST_URL: String = "https://oauth2.googleapis.com/token"
    private val GOOGLE_USERINFO_REQUEST_URL: String ="https://www.googleapis.com/oauth2/v1/userinfo";

    fun getOAuthRedirectUrl(): String {
        return "$GOOGLE_LOGIN_URL" +
                "?response_type=code&" +
                "scope=${GOOGLE_SCOPE}&" +
                "client_id=${GOOGLE_CLIENT_ID}&" +
                "redirect_uri=${GOOGLE_CALLBACK_URL}"
    }

    fun getAccessToken(code: String): GoogleAuthToken {
        val stringAccessToken = getStringAccessToken(code)
        return objectMapper.readValue(stringAccessToken.body, GoogleAuthToken::class.java)
    }

    private fun getStringAccessToken(code: String): ResponseEntity<String> {
        val restTemplate: RestTemplate = RestTemplate()
        val params = mapOf<String, String>(
            "code" to code,
            "client_id" to GOOGLE_CLIENT_ID!!,
            "client_secret" to GOOGLE_CLIENT_SECRET!!,
            "redirect_uri" to GOOGLE_CALLBACK_URL!!,
            "grant_type" to "authorization_code"
        )
        return restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, params, String::class.java)
    }

    fun getUserInfo(googleAuthToken: GoogleAuthToken): GoogleUser {
        val stringUserInfo = getStringUserInfo(googleAuthToken)
        return objectMapper.readValue(stringUserInfo.body, GoogleUser::class.java)
    }

    private fun getStringUserInfo(googleAuthToken: GoogleAuthToken): ResponseEntity<String> {
        val restTemplate: RestTemplate = RestTemplate()
        val headers: HttpHeaders = HttpHeaders()
        headers.add("Authorization", "Bearer ${googleAuthToken.access_token}")
        val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(headers)
        return restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String::class.java)
    }
}