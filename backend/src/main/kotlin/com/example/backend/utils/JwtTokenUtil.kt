package com.example.backend.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import kotlin.collections.HashMap


@Component
class JwtTokenUtil {
    companion object {
        private const val JWT_TOKEN_VALIDITY: Long = 5 * 60 * 60
    }

    @Value("\${jwt.secret}")
    var secret: String? = null

    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token, Claims::getSubject)
    }

    fun getIssuedAtDateFromToken(token: String): Date {
        return getClaimFromToken<Date>(token, Claims::getIssuedAt)
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken<Date>(token, Claims::getExpiration)
    }

    fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration: Date = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        // here you specify tokens, for that the expiration is ignored
        return false
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, secret).compact()
    }

    fun canTokenBeRefreshed(token: String): Boolean {
        return !isTokenExpired(token) || ignoreTokenExpiration(token)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun getUsernameFromRequest(request: HttpServletRequest): String {
        val requestToken = request.getHeader("Authorization") as String
        val token = requestToken.replace("Bearer ", "")
        return getUsernameFromToken(token)
    }
}