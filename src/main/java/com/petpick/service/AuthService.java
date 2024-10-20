package com.petpick.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public ResponseEntity<?> generateTokens(String userEmail, HttpServletResponse response) {
        // JWT 토큰 생성 로직
        // 쿠키 설정 및 응답 반환
        return null;
    }
}
