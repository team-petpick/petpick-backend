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
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @Value("${cloud.aws.region.static}") // Use region from application properties
    private String regionName;

    public List<ProductImgResponse> uploadImages(
            Integer productId, List<Integer> thumbNails, MultipartFile[] files) throws IOException {

        List<ProductImgResponse> responses = new ArrayList<>();

        // Validate that the number of thumbnail flags matches the number of files
        if (thumbNails.size() != files.length) {
            throw new IllegalArgumentException("Number of thumbNails must match number of files");
        }

        // Retrieve the Product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            Integer thumbNailFlag = thumbNails.get(i);

            String fileName = generateFileName(file);
            String s3Key = fileName;

            boolean isThumbnail = thumbNailFlag != null && thumbNailFlag == 1;

            // Upload file to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .acl(ObjectCannedACL.PUBLIC_READ) // Optional: Set object to be publicly readable
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Save info in database
            String fileUrl = getFileUrl(bucketName, s3Key);

            if (isThumbnail) {
                // Unset existing thumbnails for the product
                unsetExistingThumbnails(product);
            }

            ProductImg productImg = ProductImg.builder()
                    .product(product)
                    .productImgUrl(fileUrl)
                    .productImgThumb(isThumbnail ? 1 : 0)
                    .build();

            ProductImg savedProductImg = productImgRepository.save(productImg);

            responses.add(new ProductImgResponse(savedProductImg));
        }

        return responses;
    }

    public Optional<ProductImgResponse> getImage(Integer id) {
        return productImgRepository.findById(id)
                .map(ProductImgResponse::new);
    }

    public ProductImgResponse updateImage(
            Integer id, Integer thumbNail, MultipartFile file) throws IOException {

        ProductImg productImg = productImgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Delete old image from S3
        deleteImageFromS3(productImg.getProductImgUrl());

        // Upload new image to S3
        String newFileName = generateFileName(file);
        String newS3Key = newFileName;

        boolean isThumbnail = thumbNail != null && thumbNail == 1;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(newS3Key)
                .acl(ObjectCannedACL.PUBLIC_READ) // Optional: Set object to be publicly readable
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Update any existing thumbnails for the product if the new image is a thumbnail
        if (isThumbnail) {
            unsetExistingThumbnails(productImg.getProduct());
        }

        // Update image URL and thumbnail flag by creating a new instance
        String fileUrl = getFileUrl(bucketName, newS3Key);

        // Create a new ProductImg instance with updated values
        ProductImg updatedProductImg = productImg.withUpdatedFields(fileUrl, isThumbnail ? 1 : 0);

        ProductImg savedProductImg = productImgRepository.save(updatedProductImg);

        return new ProductImgResponse(savedProductImg);
    }

    public void deleteImage(Integer id) {
        ProductImg productImg = productImgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Delete image from S3
        deleteImageFromS3(productImg.getProductImgUrl());

        // Delete record from database
        productImgRepository.deleteById(id);
    }

    private void deleteImageFromS3(String imageUrl) {
        String fileName = extractFileNameFromUrl(imageUrl);
        String s3Key = fileName;

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private void unsetExistingThumbnails(Product product) {
        List<ProductImg> existingThumbnails = productImgRepository.findByProductAndProductImgThumb(product, 1);
        for (ProductImg existingThumbnail : existingThumbnails) {
            // Create a new instance with updated thumbnail flag
            ProductImg updatedImg = existingThumbnail.withUpdatedFields(existingThumbnail.getProductImgUrl(), 0);
            productImgRepository.save(updatedImg);
        }
    }

    private String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID().toString() + "-" +
                multiPart.getOriginalFilename().replace(" ", "_");
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private String getFileUrl(String bucketName, String key) {
        Region region = Region.of(regionName); // Use the region from properties

        // Build S3Utilities with region
        S3Utilities utilities = S3Utilities.builder()
                .region(region)
                .build();

        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        URL url = utilities.getUrl(request);

        return url.toString();
    }
}
