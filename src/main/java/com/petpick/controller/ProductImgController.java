package com.petpick.controller;

import com.petpick.model.ProductImgResponse;
import com.petpick.service.image.ProductImgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product-images")
public class ProductImgController {

    @Autowired
    private ProductImgService productImgService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductImgResponse> uploadImages(
            @RequestParam("productId") Integer productId,
            @RequestParam("thumbNails") List<Integer> thumbNails,
            @RequestParam("files") MultipartFile[] files) throws IOException {

        return productImgService.uploadImages(productId, thumbNails, files);
    }

    @GetMapping("/{id}")
    public ProductImgResponse getImage(@PathVariable Integer id) {
        return productImgService.getImage(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductImgResponse updateImage(
            @PathVariable Integer id,
            @RequestParam(value = "thumbNail", required = false) Integer thumbNail,
            @RequestParam("file") MultipartFile file) throws IOException {

        return productImgService.updateImage(id, thumbNail, file);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Integer id) {
        productImgService.deleteImage(id);
    }
}
