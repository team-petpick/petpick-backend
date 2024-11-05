package com.petpick.controller;

import com.petpick.model.UserLikesProductListResponse;
import com.petpick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/likes")
    public ResponseEntity<List<UserLikesProductListResponse>> getProductLikes(
            @RequestAttribute Integer userId
    ) {
        List<UserLikesProductListResponse> userLikesProductListResponse = userService.getUserLikesProductList(userId);
        return ResponseEntity.ok(userLikesProductListResponse);
    }

}
