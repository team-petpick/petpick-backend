package com.petpick.service.image;

import com.petpick.domain.Product;
import com.petpick.domain.ProductImg;
import com.petpick.model.ProductImgResponse;
import com.petpick.repository.ProductImgRepository;
import com.petpick.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final ProductRepository productRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public ProductImgService(
            ProductImgRepository productImgRepository,
            ProductRepository productRepository,
            S3Client s3Client) {
        this.productImgRepository = productImgRepository;
        this.productRepository = productRepository;
        this.s3Client = s3Client;
    }

    public List<ProductImgResponse> uploadImages(
            Integer productId, List<Integer> thumbNails, List<Integer> isDesc, MultipartFile[] files) throws IOException {

        List<ProductImgResponse> responses = new ArrayList<>();

        if (thumbNails.size() != files.length || isDesc.size() != files.length) {
            throw new IllegalArgumentException("Number of thumbNails and isDesc flags must match number of files");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            Integer thumbNailFlag = thumbNails.get(i);
            Integer isDescFlag = isDesc.get(i);

            String fileName = generateFileName(file);
            String s3Key = fileName;

            boolean isThumbnail = thumbNailFlag != null && thumbNailFlag == 1;
            boolean isDescription = isDescFlag != null && isDescFlag == 1;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .acl(ObjectCannedACL.PUBLIC_READ) // Optional: Set object to be publicly readable
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = getFileUrl(bucketName, s3Key);

            if (isThumbnail) {
                // Unset existing thumbnails for the product
                unsetExistingThumbnails(product);
            }

            if (isDescription) {
                unsetExistingDescriptionImages(product);
            }

            ProductImg productImg = ProductImg.builder()
                    .product(product)
                    .productImgUrl(fileUrl)
                    .productImgThumb(isThumbnail ? 1 : 0)
                    .descImgStatus(isDescription ? 1 : 0)
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

    public List<ProductImgResponse> getImagesByProductId(Integer productId) {
        List<ProductImg> images = productImgRepository.findAllByProduct_productId(productId);

        return images.stream()
                .map(ProductImgResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<ProductImgResponse> getDescImage(Integer id) {
        return productImgRepository.findById(id)
                .filter(img -> img.getDescImgStatus() != null && img.getDescImgStatus() == 1)
                .map(ProductImgResponse::new);
    }

    public ProductImgResponse updateImage(
            Integer id, Integer thumbNail, Integer isDesc, MultipartFile file) throws IOException {

        ProductImg productImg = productImgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        deleteImageFromS3(productImg.getProductImgUrl());

        String newFileName = generateFileName(file);
        String newS3Key = newFileName;

        boolean isThumbnail = thumbNail != null && thumbNail == 1;
        boolean isDescription = isDesc != null && isDesc == 1;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(newS3Key)
                .acl(ObjectCannedACL.PUBLIC_READ) // Optional: Set object to be publicly readable
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Update any existing thumbnails or description images if necessary
        if (isThumbnail) {
            unsetExistingThumbnails(productImg.getProduct());
        }

        if (isDescription) {
            unsetExistingDescriptionImages(productImg.getProduct());
        }

        String fileUrl = getFileUrl(bucketName, newS3Key);

        ProductImg updatedProductImg = productImg.withUpdatedFields(fileUrl,
                isThumbnail ? 1 : productImg.getProductImgThumb(),
                isDescription ? 1 : productImg.getDescImgStatus());

        ProductImg savedProductImg = productImgRepository.save(updatedProductImg);

        return new ProductImgResponse(savedProductImg);
    }

    public void deleteImage(Integer id) {
        ProductImg productImg = productImgRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

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
            ProductImg updatedImg = existingThumbnail.withUpdatedFields(
                    existingThumbnail.getProductImgUrl(),
                    0,
                    existingThumbnail.getDescImgStatus()
            );
            productImgRepository.save(updatedImg);
        }
    }

    private void unsetExistingDescriptionImages(Product product) {
        List<ProductImg> existingDescImages = productImgRepository.findByProductAndDescImgStatus(product, 1);
        for (ProductImg existingDescImg : existingDescImages) {
            // Create a new instance with updated description flag
            ProductImg updatedImg = existingDescImg.withUpdatedFields(
                    existingDescImg.getProductImgUrl(),
                    existingDescImg.getProductImgThumb(),
                    0
            );
            productImgRepository.save(updatedImg);
        }
    }

    private String generateFileName(MultipartFile multiPart) {
        return UUID.randomUUID().toString() + "-" +
                Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private String getFileUrl(String bucketName, String key) {
        S3Utilities utilities = s3Client.utilities();

        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        URL url = utilities.getUrl(request);

        return url.toString();
    }
}
