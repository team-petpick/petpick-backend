//package com.petpick.controller;
//
//import com.petpick.domain.User;
//import com.petpick.global.exception.BaseException;
//import com.petpick.global.exception.errorCode.AuthErrorCode;
//import com.petpick.global.response.SuccessResponse;
//import com.petpick.model.AuthorizationCodeResponse;
//import com.petpick.model.GoogleTokenResponse;
//import com.petpick.model.GoogleUserInfoResponse;
//import com.petpick.service.auth.GoogleTokenService;
//import com.petpick.service.user.GoogleUserService;
//import com.petpick.service.auth.TokenProvider;
//import com.petpick.service.user.UserService;
//import com.petpick.util.CookieUtil;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//import java.util.Optional;
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
//    /*
//     * request for first login or refresh token expires
//     * */
//    @PostMapping("/google")
//    public ResponseEntity<?> googleCallback(@RequestBody AuthorizationCodeResponse authorizationCodeResponse, HttpServletResponse httpServletResponse) {
//        String code = authorizationCodeResponse.getCode();
//
//        if (code == null || code.isEmpty()) {
//            throw new BaseException(AuthErrorCode.INVALID_AUTHORIZATION_CODE);
//        }
//
//        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
//
//        // Request access token from Google
//        GoogleTokenResponse googleTokenResponse = googleTokenService.exchangeCodeForToken(decodedCode);
//
//        // Use the access token to fetch user info from Google
//        GoogleUserInfoResponse googleUserInfoResponse = googleUserService.getUserInfo(googleTokenResponse.getAccessToken());
//
//        // find user based on email
//        User user = userService.findOrCreateUser(googleUserInfoResponse);
//
//        // create own access token and refresh token
//        String accessToken = tokenProvider.createAccessToken(user);
//        String refreshToken = tokenProvider.createRefreshToken(user);
//
//        // save the refresh token to DB
//        userService.saveRefreshToken(user, refreshToken);
//
//        CookieUtil.addCookie(httpServletResponse, "refreshToken", refreshToken, cookieMaxAge);
//
//        // include token in response
//        return ResponseEntity.ok(Map.of("access_token", accessToken));
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
//        /* 쿠키에서 가져오고
//            => 쿠키에서 리프레시 토큰 값 뽑고
//            => 리프레시 토큰 값으로 사용자 찾고
//            => 사용자 찾아서 리프레시 토큰 컬럼 제거하고
//            => 쿠키도 삭제
//         */
//        Optional<Cookie> refreshTokenCookie = CookieUtil.getCookie(request, "refreshToken");
//
//        if (refreshTokenCookie.isPresent()) {
//            String refreshToken = refreshTokenCookie.get().getValue();
//
//            Optional<User> userOptional = userService.findByRefreshToken(refreshToken);
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//                userService.deleteRefreshToken(user);
//            } else {
//                throw new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN);
//            }
//        } else {
//            throw new BaseException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
//        }
//
//        CookieUtil.deleteCookie(response, "refreshToken");
//
//        return ResponseEntity.ok(SuccessResponse.success("Successfully logged out"));
//    }
//
//    /*
//     * request when access token expires
//     * */
//    @PostMapping("/token")
//    public ResponseEntity<?> regenerateJWT(HttpServletRequest request) {
//
//        // get the refresh token from the cookie
//        Optional<Cookie> refreshTokenCookie = CookieUtil.getCookie(request, "refreshToken");
//
//        if (refreshTokenCookie.isEmpty()) {
//            throw new BaseException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
//        }
//
//        String refreshToken = refreshTokenCookie.get().getValue();
//
//        // validate refresh token
//        if (!tokenProvider.validateToken(refreshToken)) {
//            throw new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        String userEmail = tokenProvider.getUserEmailFromToken(refreshToken);
//
//        Optional<User> userOptional = userService.findByUserEmail(userEmail);
//
//        if (userOptional.isEmpty()) {
//            throw new BaseException(AuthErrorCode.INVALID_USER_EMAIL);
//        }
//
//        User user = userOptional.get();
//
//        if (!refreshToken.equals(user.getUserRefreshToken())) {
//            throw new BaseException(AuthErrorCode.REFRESH_TOKEN_MISMATCH);
//        }
//
//        String newAccessToken = tokenProvider.createAccessToken(user);
//
//        return ResponseEntity.ok(Map.of("access_token", newAccessToken));
//    }
//}