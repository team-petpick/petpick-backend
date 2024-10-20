package com.petpick.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User != null) {
            // Extract user attributes
            String name = oauth2User.getAttribute("name");
            String email = oauth2User.getAttribute("email");
            String picture = oauth2User.getAttribute("picture"); // For Google OAuth2

            // Create a user response object
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("picture", picture);

            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(401).body("User is not authenticated");
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String authorizationCode = request.get("code");
        if (authorizationCode == null) {
            return ResponseEntity.badRequest().body("Authorization code is missing");
        }

        // google token endpoint url
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        // request parameter
        Map<String, String> params = new HashMap<>();
        params.put("code", authorizationCode);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Map<String, String>> tokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, tokenRequest, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> tokenResponse = response.getBody();
            String accessToken = (String) tokenResponse.get("access_token");

            // 사용자 정보 엔드포인트 호출
            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v2/userinfo";

            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<?> entity = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    userInfoEndpoint,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> userInfo = userInfoResponse.getBody();

                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.status(userInfoResponse.getStatusCode()).body("사용자 정보 가져오기 실패");
            }
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("토큰 교환 실패");
        }
    }
}