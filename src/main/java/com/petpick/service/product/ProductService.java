package com.petpick.service.product;

import com.petpick.domain.Likes;
import com.petpick.domain.Product;
import com.petpick.domain.type.ProductStatus;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.model.ProductDetailResponse;
import com.petpick.repository.LikesRepository;
import com.petpick.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LikesRepository likesRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductsWithOn() {
        return productRepository.findByProductStatus(ProductStatus.ON);
    }

    public ProductDetailResponse getProductById(Integer id) { // = product id
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BaseException(ProductErrorCode.PRODUCT_NOT_FOUND));
        int likesCount = likesRepository.countByProduct_ProductId(id);

        return new ProductDetailResponse(product, likesCount);
    }

}
