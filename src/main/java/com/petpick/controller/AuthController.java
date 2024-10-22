package com.petpick.controller;

import com.petpick.global.response.ErrorResponse;
import com.petpick.global.response.SuccessResponse;
import com.petpick.model.GoogleTokenResponse;
import com.petpick.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<?> exchangeCode(@RequestBody String authorizationCode) {
        if (authorizationCode == null || authorizationCode.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ErrorResponse.error("400", "Authorization code is missing")
            );
        }

        try {
            GoogleTokenResponse googleTokenResponse = authService.exchangeCodeForToken(authorizationCode);
            return ResponseEntity.ok(SuccessResponse.success(googleTokenResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ErrorResponse.error("500", e.getMessage())
            );
        }
    }

}
