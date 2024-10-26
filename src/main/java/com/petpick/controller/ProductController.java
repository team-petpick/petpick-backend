package com.petpick.controller;

import com.petpick.domain.Product;
import com.petpick.model.ProductDetailResponse;
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
    public List<Product> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products;
    }

    /*
    * Detail Product Page
    * */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable Integer id) {
        ProductDetailResponse productDetailResponse = productService.getProductById(id);
        return ResponseEntity.ok(productDetailResponse);
    }
}
