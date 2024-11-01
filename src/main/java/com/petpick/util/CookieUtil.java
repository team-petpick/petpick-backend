package com.petpick.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(false); // https 등록 시 true로 변경
        cookie.setSecure(false);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(name))
                    .findFirst()
                    .map(cookie -> {
                        try {
                            String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
                            cookie.setValue(decodedValue);
                            return cookie;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return cookie;
                        }
                    });
        }
        return Optional.empty();
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(0);  // To force cookies to expire

        response.addCookie(cookie);
    }
}