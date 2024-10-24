//package com.petpick.controller;
//
//import com.petpick.domain.User;
//import com.petpick.global.response.ErrorResponse;
//import com.petpick.global.response.SuccessResponse;
//import com.petpick.model.GoogleTokenResponse;
//import com.petpick.model.GoogleUserInfoResponse;
//import com.petpick.service.auth.GoogleTokenService;
//import com.petpick.service.user.GoogleUserService;
//import com.petpick.service.auth.TokenProvider;
//import com.petpick.service.user.UserService;
//import com.petpick.util.CookieUtil;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/auth")
//public class AuthController {
//
//    private final TokenProvider tokenProvider;
//    private final UserService userService;
//    private final GoogleUserService googleUserService;
//    private final GoogleTokenService googleTokenService;
//
//    @Value("${cookie.cookie-max-age}")
//    private int cookieMaxAge;
//
//    @PostMapping("/google")
//    public ResponseEntity<?> exchangeCode(@RequestBody String authorizationCode, HttpServletResponse httpServletResponse) {
//        if (authorizationCode == null || authorizationCode.isEmpty()) {
//            return ResponseEntity.badRequest().body(
//                    ErrorResponse.error("400", "Authorization code is missing")
//            );
//        }
//
//        try {
//            // Request access token from Google
//            GoogleTokenResponse googleTokenResponse = googleTokenService.exchangeCodeForToken(authorizationCode);
//
//            // Use the access token to fetch user info from Google
//            GoogleUserInfoResponse googleUserInfoResponse = googleUserService.getUserInfo(googleTokenResponse.getAccessToken());
//
//            // find user base on email
//            User user = userService.findOrCreateUser(googleUserInfoResponse);
//
//            // create own access token and refresh token
//            String accessToken = tokenProvider.createAccessToken(user);
//            String refreshToken = tokenProvider.createRefreshToken(user);
//
//            // save the refresh token to DB
//            userService.saveRefreshToken(user, refreshToken);
//
//            CookieUtil.addCookie(httpServletResponse, "refreshToken", refreshToken, cookieMaxAge);
//
//            // include token in response
//            return ResponseEntity.ok(SuccessResponse.success(Map.of(
//                    "accessToken", accessToken
//            )));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    ErrorResponse.error("500", e.getMessage())
//            );
//        }
//    }
//
////    @PostMapping("/logout")
////    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
////
////        CookieUtil.deleteCookie(response, "refreshToken");
////
////        return ResponseEntity.ok(SuccessResponse.success("Successfully logged out"));
////    }
//
//}