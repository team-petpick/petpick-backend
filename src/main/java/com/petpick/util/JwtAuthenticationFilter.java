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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = tokenProvider.resolveAccessToken(request);

//        System.out.println(token);
//        System.out.println(tokenProvider.validateToken(token));

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
        String path = request.getRequestURI();
        boolean shouldNotFilter = path.equals("/api/v1/auth/google") ||
                path.equals("/api/v1/auth/token") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/v2/api-docs") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars") ||
                path.startsWith("/api/v1/products"); // 수정된 부분

        return shouldNotFilter;
    }
}
