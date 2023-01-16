package com.wafflestudio.team03server.core.user.service

import com.wafflestudio.team03server.common.Exception401
import com.wafflestudio.team03server.common.Exception403
import com.wafflestudio.team03server.common.Exception404
import com.wafflestudio.team03server.core.user.entity.User
import com.wafflestudio.team03server.core.user.repository.UserRepository
import com.wafflestudio.team03server.properties.AuthProperties
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@EnableConfigurationProperties(AuthProperties::class)
class AuthTokenService(
    private val authProperties: AuthProperties,
    private val userRepository: UserRepository,
) {
    private val tokenPrefix = "Bearer "
    private val signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret.toByteArray())

    private fun generateTokenBuilderByEmailAndExpiration(email: String, expiration: Long): JwtBuilder {
        val claims = Jwts.claims()
        val expiryDate = Date(Date().time + expiration)
        return Jwts.builder()
            .signWith(signingKey)
            .setClaims(claims)
            .setSubject(email)
            .setIssuer(authProperties.issuer)
            .setExpiration(expiryDate)
            .setIssuedAt(Date())
    }

    fun generateAccessTokenAndRefreshToken(email: String, user: User): AuthToken {
        val accessTokenBuilder = generateTokenBuilderByEmailAndExpiration(email, authProperties.atExpiration)
        val refreshTokenBuilder = generateTokenBuilderByEmailAndExpiration(email, authProperties.rtExpiration)
        val accessToken = accessTokenBuilder.setAudience(user.username).claim("type", "access").compact()
        val refreshToken = refreshTokenBuilder.setAudience(user.username).claim("type", "refresh").compact()
        user.refreshToken = refreshToken
        return AuthToken(accessToken, refreshToken)
    }

    @Transactional(noRollbackFor = [Exception403::class])
    fun verifyToken(authToken: String, isRefreshToken: Boolean = false) {
        try {
            val claims = parse(authToken).body
            if (isRefreshToken) {
                var savedToken = userRepository.findByEmail(claims.subject)?.refreshToken
                if (savedToken == null || savedToken != authToken) {
                    savedToken = null
                    throw Exception403("접근이 거부되었습니다.")
                }
            } else {
                if (claims["type"] != "access") {
                    throw Exception401("JWT 토큰 타입이 잘못되었습니다.")
                }
            }
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

    fun getCurrentUserEmail(authToken: String): String {
        return parse(authToken).body.subject
    }

    private fun parse(authToken: String): Jws<Claims> {
        val prefixRemoved = authToken.replace(tokenPrefix, "").trim { it <= ' ' }
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(prefixRemoved)
    }
}
