package com.petpick.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * controller need to be fixed!
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

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
}
