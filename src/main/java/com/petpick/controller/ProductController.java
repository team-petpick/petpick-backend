package com.petpick.controller;

import com.petpick.domain.Product;
import com.petpick.model.ProductDetailResponse;
import com.petpick.model.ProductListResponse;
import com.petpick.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    /*
    * Total Product List
    * */
    @GetMapping("/products")
    public ResponseEntity<List<ProductListResponse>> getAllProducts() {
        List<ProductListResponse> productsListResponse = productService.getAllProducts();
        return ResponseEntity.ok(productsListResponse);
    }

    /*
    * Detail Product Page
    * */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Integer id) {
        ProductDetailResponse productDetailResponse = productService.getProductById(id);
        return ResponseEntity.ok(productDetailResponse);
    }

    /*
    * Press Like button
    * */
    @PostMapping("/products/{productId}/like")
    public ResponseEntity<ProductDetailResponse> addProductLike(@PathVariable Integer productId, @RequestBody Product product) {
        return null;
    }

    /*
    * Cancel Like button
    * */
    @DeleteMapping("/products/{productId}/like")
    public ResponseEntity<ProductDetailResponse> deleteProductLike(@PathVariable Integer productId, @RequestBody Product product) {
        return null;
    }
}
