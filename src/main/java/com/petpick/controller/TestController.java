package com.petpick.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @RequestMapping("/")
    public RedirectView redirectToHello() {
        return new RedirectView("/api/hello");
    }
    @GetMapping("/api/hello")
    public Map<String, String> helloApiInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "api 는 \"back.petpick.store/api/v1/~\" 로 구성되어 있습니다");
        return response;
    }
}