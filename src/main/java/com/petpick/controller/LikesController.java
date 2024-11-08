package com.petpick.controller;

import com.petpick.service.likes.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/like/{productId}")
    public ResponseEntity<String> toggleProductLike(
            @PathVariable String productId,
            @RequestAttribute Integer userId
    ) {
        Integer convertedProductId = Integer.parseInt(productId);
        boolean isLiked = likesService.toggleLike(convertedProductId, userId);

        String message = isLiked ? "Successfully added like to the product" : "Successfully removed like from the product";
        return ResponseEntity.ok(message);
    }
}
