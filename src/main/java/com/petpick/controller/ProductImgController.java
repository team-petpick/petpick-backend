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

    private ProductImgService productImgService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ProductImgResponse> uploadImages(
            @RequestParam("productId") Integer productId,
            @RequestParam("thumbNails") List<Integer> thumbNails,
            @RequestParam("isDesc") List<Integer> isDesc, // Ensure this parameter is provided
            @RequestParam("files") MultipartFile[] files) throws IOException {

        return productImgService.uploadImages(productId, thumbNails, isDesc, files);
    }

    @GetMapping("/{id}")
    public ProductImgResponse getImage(@PathVariable Integer id) {
        return productImgService.getImage(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @GetMapping("/desc/{id}")
    public ProductImgResponse getDescImage(@PathVariable Integer id) {
        return productImgService.getDescImage(id)
                .orElseThrow(() -> new RuntimeException("Description Image not found"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductImgResponse updateImage(
            @PathVariable Integer id,
            @RequestParam(value = "thumbNail", required = false) Integer thumbNail,
            @RequestParam(value = "isDesc", required = false) Integer isDesc, // Added parameter
            @RequestParam("file") MultipartFile file) throws IOException {

        return productImgService.updateImage(id, thumbNail, isDesc, file);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable Integer id) {
        productImgService.deleteImage(id);
    }
}
