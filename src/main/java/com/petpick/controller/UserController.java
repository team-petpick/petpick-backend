package com.petpick.controller;

import com.petpick.repository.entity.User;
import com.petpick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setUserId(null); // Ensure the ID is null for a new entity
        userService.createUser(user);
        return ResponseEntity.status(201).body(user);
    }
}
