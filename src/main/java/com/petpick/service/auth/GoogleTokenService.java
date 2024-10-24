//package com.petpick.service.auth;
//
//import com.petpick.model.GoogleTokenResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestClientException;
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
//    @Value("${BACKEND_DEVELOP_URL}")
//    private String redirectUri;
//
//    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
//
//    public GoogleTokenResponse exchangeCodeForToken(String authorizationCode) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
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
//                    GoogleTokenResponse.class // 응답 매핑할 클래스
//            );
//
//            return response.getBody();
//
//        } catch (HttpClientErrorException e) {
//            // 클라이언트 측 오류 (4xx)
//            throw new IllegalStateException("Client error: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
//        } catch (HttpServerErrorException e) {
//            // 서버 측 오류 (5xx)
//            throw new IllegalStateException("Server error: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
//        } catch (RestClientException e) {
//            // 그 외 네트워크 오류
//            throw new IllegalStateException("Error during token exchange with Google", e);
//        }
//    }
//
//}