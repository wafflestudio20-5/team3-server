package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception401
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.user.repository.UserRepository
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.util.*

@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
    private val userRepository: UserRepository
) {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    fun generateTokenByEmail(email: String): AuthToken {
        val claims = Jwts.claims()
        claims["aud"] = userRepository.findByEmail(email)?.username ?: throw Exception404("User not found")
        val expiryDate = Date(Date().time + authProperties.jwtExpiration)
        val resultToken = Jwts.builder()
            .signWith(signingKey)
            .setClaims(claims)
            .setSubject(email)
            .setIssuer(authProperties.issuer)
            .setExpiration(expiryDate)
            .setIssuedAt(Date())
            .compact()

        return AuthToken(resultToken)
    }

    fun verifyToken(authToken: String) {
        try {
            val claims = parse(authToken).body
        } catch (e: MalformedJwtException) {
            throw Exception401("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            throw Exception401("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            throw Exception401("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            throw Exception401("JWT 토큰이 잘못되었습니다.")
        }
    }

    fun getCurrentUserId(authToken: String): Long {
        val userEmail = parse(authToken).body.subject
        return userRepository.findByEmail(userEmail)?.id ?: throw Exception404("User Authentication Failed")
    }

    private fun parse(authToken: String): Jws<Claims> {
        val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
    }
}
