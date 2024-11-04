package com.petpick.util;

import com.petpick.service.auth.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = tokenProvider.resolveAccessToken(request);

        if (token != null && tokenProvider.validateToken(token) != null) {
            // 토큰 디코드하여 클레임 추출
            Claims claims = tokenProvider.validateToken(token);

            // 추출한 정보를 요청 속성에 추가
            request.setAttribute("userId", claims.getSubject()); // userId가 subject에 저장된 경우
            request.setAttribute("userEmail", claims.get("email")); // email 클레임이 있는 경우

            // SecurityContext에 인증 정보 설정
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), // principal (userId)
                    null, // credentials
                    null  // authorities (권한)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // 토큰이 유효하지 않거나 없는 경우 401 Unauthorized 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing access token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 로그인, 토큰 재발급 등 토큰 검증이 필요 없는 경로를 예외 처리
        String path = request.getRequestURI();
        return path.equals("/api/v1/auth/google") || path.equals("/api/v1/auth/token");
    }
}
