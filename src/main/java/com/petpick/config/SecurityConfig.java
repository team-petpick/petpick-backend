//package com.petpick.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(request -> {
//                    CorsConfiguration config = new CorsConfiguration();
//                    config.setAllowedOrigins(List.of("http://localhost:5173")); // 프론트엔드 URL
//                    config.setAllowedMethods(List.of("*"));
//                    config.setAllowedHeaders(List.of("*"));
//                    config.setAllowCredentials(true);
//                    config.addExposedHeader("Cross-Origin-Opener-Policy");
//                    config.addExposedHeader("Cross-Origin-Embedder-Policy");
//                    return config;
//                }))
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers("/auth/**", "/oauth2/**").permitAll()
//                        .anyRequest().authenticated()
//                )
////                JWT Filter 구현
////                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil,redisUtil), UsernamePasswordAuthenticationFilter.class)
//                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(authorization -> authorization
//                                .baseUri("/oauth2/authorize")
//                        )
//                        .redirectionEndpoint(redirection -> redirection
//                                .baseUri("/oauth2/callback/*")
//                        )
//                );
//        return http.build();
//    }
//}