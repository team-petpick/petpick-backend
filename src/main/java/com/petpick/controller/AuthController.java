package com.petpick.controller;

import com.petpick.global.response.ErrorResponse;
import com.petpick.global.response.SuccessResponse;
import com.petpick.model.GoogleTokenResponse;
import com.petpick.model.GoogleUserInfoResponse;
import com.petpick.service.GoogleTokenService;
import com.petpick.service.GoogleUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final GoogleTokenService googleTokenService;
    private final GoogleUserService googleUserService;

    @PostMapping("/google")
    public ResponseEntity<?> exchangeCode(@RequestBody String authorizationCode) {
        if (authorizationCode == null || authorizationCode.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ErrorResponse.error("400", "Authorization code is missing")
            );
        }

        try {
            // Request access token from Google
            GoogleTokenResponse googleTokenResponse = googleTokenService.exchangeCodeForToken(authorizationCode);

            // Use the access token to fetch user info from Google
            GoogleUserInfoResponse googleUserInfoResponse = googleUserService.getUserInfo(googleTokenResponse.getAccessToken());

            return ResponseEntity.ok(SuccessResponse.success(googleUserInfoResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ErrorResponse.error("500", e.getMessage())
            );
        }
    }

}
