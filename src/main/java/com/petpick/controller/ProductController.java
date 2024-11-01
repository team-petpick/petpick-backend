package com.petpick.controller;

import com.petpick.model.ProductDetailResponse;
import com.petpick.model.ProductListResponse;
import com.petpick.service.auth.TokenProvider;
import com.petpick.service.likes.LikesService;
import com.petpick.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    private final TokenProvider tokenProvider;
    private final LikesService likesService;


    @GetMapping("/products")
    public ResponseEntity<Page<ProductListResponse>> getProductsList(
            @RequestParam(required = false) String type,
            @RequestParam(name = "category", required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(defaultValue = "createAt_desc") String sort,
            @RequestParam(required = false) String search
    ) {
        Page<ProductListResponse> productsListResponse = productService.getProductsList(type, categoryId, page, size, sort, search);
        return ResponseEntity.ok(productsListResponse);
    }


    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Integer productId) {
        ProductDetailResponse productDetailResponse = productService.getProductById(productId);
        return ResponseEntity.ok(productDetailResponse);
    }


    @PostMapping("/products/{productId}/like")
    public ResponseEntity<String> addProductLike(@PathVariable Integer productId, HttpServletRequest request) {

        String accessToken = tokenProvider.resolveAccessToken(request);
        String userEmail = tokenProvider.getUserEmailFromToken(accessToken);

        likesService.addLike(productId, userEmail);


        return ResponseEntity.ok("Successfully added like to the product");
    }


    @DeleteMapping("/products/{productId}/like")
    public ResponseEntity<String> deleteProductLike(@PathVariable Integer productId, HttpServletRequest request) {
        String accessToken = tokenProvider.resolveAccessToken(request);
        String userEmail = tokenProvider.getUserEmailFromToken(accessToken);

        likesService.removeLike(productId, userEmail);

        return ResponseEntity.ok("Successfully deleted like to the product");
    }
}
