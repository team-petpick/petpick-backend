package com.petpick.controller;

import com.petpick.model.ProductImgResponse;
import com.petpick.service.image.ProductImgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/product-images")
public class ProductImgController {

    @Autowired
    private ProductImgService productImgService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductImgResponse uploadImage(@RequestParam("productId") Integer productId,
                                          @RequestPart(value = "file") MultipartFile file) throws IOException {
        return productImgService.uploadImage(productId, file);
    }

    @GetMapping("/{id}")
    public ProductImgResponse getImage(@PathVariable Integer id) {
        return productImgService.getImage(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductImgResponse updateImage(@PathVariable Integer id,
                                          @RequestPart(value = "file") MultipartFile file) throws IOException {
        return productImgService.updateImage(id, file);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Integer id) {
        productImgService.deleteImage(id);
    }
}
