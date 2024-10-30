package com.petpick.service.image;

import com.petpick.domain.Product;
import com.petpick.domain.ProductImg;
import com.petpick.model.ProductImgResponse;
import com.petpick.repository.ProductImgRepository;
import com.petpick.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductImgService {

    @Autowired
    private ProductImgRepository productImgRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public ProductImgResponse uploadImage(Integer productId, MultipartFile file) throws IOException {
        String fileName = generateFileName(file);

        // Upload file to S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Retrieve the Product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Save info in database
        String fileUrl = getFileUrl(bucketName, fileName);

        ProductImg productImg = ProductImg.builder()
                .product(product)
                .productImgUrl(fileUrl)
                .build();

        ProductImg savedProductImg = productImgRepository.save(productImg);

        return new ProductImgResponse(savedProductImg);
    }

    public Optional<ProductImgResponse> getImage(Integer id) {
        return productImgRepository.findById(id)
                .map(ProductImgResponse::new);
    }

    public ProductImgResponse updateImage(Integer id, MultipartFile file) throws IOException {
        ProductImg productImg = productImgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Delete old image from S3
        String oldFileName = extractFileNameFromUrl(productImg.getProductImgUrl());

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(oldFileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        // Upload new image to S3
        String newFileName = generateFileName(file);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(newFileName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Update image URL
        String fileUrl = getFileUrl(bucketName, newFileName);

        // Since we can't modify the entity directly (no setters), create a new entity with the same ID
        ProductImg updatedProductImg = ProductImg.builder()
                .productImgId(productImg.getProductImgId())
                .product(productImg.getProduct())
                .productImgUrl(fileUrl)
                .build();

        ProductImg savedProductImg = productImgRepository.save(updatedProductImg);

        return new ProductImgResponse(savedProductImg);
    }

    public void deleteImage(Integer id) {
        ProductImg productImg = productImgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Delete image from S3
        String fileName = extractFileNameFromUrl(productImg.getProductImgUrl());

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        // Delete record from database
        productImgRepository.deleteById(id);
    }

    private String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID().toString() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private String getFileUrl(String bucketName, String fileName) {
        S3Utilities utilities = s3Client.utilities();

        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        URL url = utilities.getUrl(request);

        return url.toString();
    }
}
