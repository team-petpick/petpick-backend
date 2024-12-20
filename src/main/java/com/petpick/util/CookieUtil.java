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
        String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Secure; Max-Age=%d; SameSite=None",
                name, value, "/", maxAge);
        response.addHeader("Set-Cookie", cookieHeader);
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(cookie -> {
                    try {
                        cookie.setValue(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return cookie;
                });
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