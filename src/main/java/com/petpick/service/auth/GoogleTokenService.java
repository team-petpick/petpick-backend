//package com.petpick.service.auth;
//
//import com.petpick.global.exception.BaseException;
//import com.petpick.global.exception.errorCode.AuthErrorCode;
//import com.petpick.model.GoogleTokenResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@RequiredArgsConstructor
//public class GoogleTokenService {
//
//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    private String clientId;
//    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
//    private String clientSecret;
//    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
//    private String redirectUri;
//
//    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
//
//    public GoogleTokenResponse exchangeCodeForToken(String authorizationCode) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // 요청 파라미터 설정
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", authorizationCode);
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUri);
//        params.add("grant_type", "authorization_code");
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        try {
//            ResponseEntity<GoogleTokenResponse> response = restTemplate.exchange(
//                    TOKEN_URL,
//                    HttpMethod.POST,
//                    request,
//                    GoogleTokenResponse.class
//            );
//            return response.getBody();
//        } catch (Exception ex) {
//            // 유효하지 않은 인가 코드를 사용한 경우 발생하는 예외를 처리
//            throw new BaseException(AuthErrorCode.INVALID_AUTHORIZATION_CODE);
//        }
//    }
//}
