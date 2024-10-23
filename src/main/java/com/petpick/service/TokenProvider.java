package com.petpick.service;

import com.petpick.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider {

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret;

    private long accessTokenExpiration = 1000L * 60 * 60 * 1;
    private long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 7;

    // create access token
    public String createAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getUserEmail());  // 사용자 이메일 정보

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUserEmail())  // 토큰의 subject로 이메일 사용
                .setIssuedAt(new Date())  // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))  // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  // 서명 알고리즘과 비밀키 설정
                .compact();
    }

    // create refresh token
    public String createRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 정보 추출
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // subject로 설정된 이메일을 추출
    }
}
