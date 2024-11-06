package com.petpick.config;

import com.petpick.service.auth.TokenProvider;
import com.petpick.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("http://localhost:3000","https://back.petpick.store", "https://petpick.netlify.app", "http://localhost:8080","https://petpick-dev.netlify.app")); // 프론트엔드 URL
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
                    config.setAllowCredentials(true);
                    config.addExposedHeader("Cross-Origin-Opener-Policy");
                    config.addExposedHeader("Cross-Origin-Embedder-Policy");
                    return config;
                }))
//                .requiresChannel(channel -> channel.anyRequest().requiresSecure()) // 모든 요청을 https로 강제하는 코드 => https 등록 시 주석 해제
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/v1/auth/google",
                                "/v1/auth/token",
                                "/v1/auth/logout",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v2/api-docs/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/v1/products",
                                "/v1/products/{productId}"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                ;
        return http.build();
    }
}
