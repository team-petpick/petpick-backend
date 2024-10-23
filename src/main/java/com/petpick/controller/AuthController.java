package com.petpick.controller;

import com.petpick.domain.User;
import com.petpick.global.response.ErrorResponse;
import com.petpick.global.response.SuccessResponse;
import com.petpick.model.GoogleTokenResponse;
import com.petpick.model.GoogleUserInfoResponse;
import com.petpick.service.GoogleTokenService;
import com.petpick.service.GoogleUserService;
import com.petpick.service.TokenProvider;
import com.petpick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final GoogleUserService googleUserService;
    private final GoogleTokenService googleTokenService;

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

            User user = userService.findOrCreateUser(googleUserInfoResponse);

            // create own access token and refresh token
            String accessToken = tokenProvider.createAccessToken(user);
            String refreshToken = tokenProvider.createRefreshToken(user);

            // save the refresh token to DB
            userService.saveRefreshToken(user, refreshToken);

            // include token in response
            return ResponseEntity.ok(SuccessResponse.success(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            )));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ErrorResponse.error("500", e.getMessage())
            );
        }
    }

}
