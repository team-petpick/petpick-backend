
package com.petpick.service.user;

import com.petpick.model.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GoogleUserService {

    private static String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    public GoogleUserInfoResponse getUserInfo(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                USER_INFO_URL,
                HttpMethod.GET,
                request,
                GoogleUserInfoResponse.class // 응답 매핑할 클래스
        );

        return response.getBody();
    }
}
