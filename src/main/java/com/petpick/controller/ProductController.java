package com.petpick.controller;

import com.petpick.domain.Product;
import com.petpick.model.ProductDetailResponse;
import com.petpick.model.ProductListResponse;
import com.petpick.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    /*
    * Filtered Product List
    * */
    @GetMapping("/products")
    public ResponseEntity<Page<ProductListResponse>> getProductsList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(defaultValue = "createAt_desc") String sort
    ) {
        Page<ProductListResponse> productsListResponse = productService.getProductsList(page, size, sort);
        return ResponseEntity.ok(productsListResponse);
    }

    /*
     * Filtered Product List
     * */
    @GetMapping("/{productType}/products")
    public ResponseEntity<Page<ProductListResponse>> getProductListByProductType(
            @PathVariable String productType,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(defaultValue = "createAt_desc") String sort
    ) {
        Page<ProductListResponse> productsListResponse = productService.getProductsListByProductType(productType, page, size, sort);
        return ResponseEntity.ok(productsListResponse);
    }

    /*
    * Detail Product Page
    * */
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Integer productId) {
        ProductDetailResponse productDetailResponse = productService.getProductById(productId);
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
