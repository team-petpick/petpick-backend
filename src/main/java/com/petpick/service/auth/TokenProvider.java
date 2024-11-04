package com.petpick.service.auth;

import com.petpick.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider {

    @Value("${jwt.jwt-key}")
    private String jwtSecretKey;
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    // create access token
    public String createAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // create refresh token
    public String createRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token) // 서명 및 만료 검증
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public Integer getUserIdFromToken(String refreshToken) {
        Claims claims = validateToken(refreshToken);
        if(claims == null) {
            throw new IllegalStateException("Invalid token");
        }
        return Integer.parseInt(claims.getSubject());
    }
}