package com.petpick.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/data")
    public Map<String, Object> getDummyData() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", 1);
        data.put("name", "Test User");
        data.put("email", "testuser@example.com");
        data.put("active", true);

        return data;
    }
}