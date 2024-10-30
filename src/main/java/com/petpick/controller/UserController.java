package com.petpick.controller;

import com.petpick.model.UserLikesProductListResponse;
import com.petpick.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    * Filtered Product List by User
    * */
    @GetMapping("/user/{userId}/likes")
    public ResponseEntity<Page<UserLikesProductListResponse>> getProductLikes(
            @PathVariable Integer userId
    ) {
        Page<UserLikesProductListResponse> userLikesProductListResponse = userService.getUserLikesProductList(userId);
        return ResponseEntity.ok(userLikesProductListResponse);
    }

}
