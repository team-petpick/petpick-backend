package com.petpick.service.product;

import com.petpick.domain.Product;
import com.petpick.domain.type.ProductStatus;
import com.petpick.global.exception.BaseException;
import com.petpick.model.ProductResponse;
import com.petpick.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByProductStatus(ProductStatus.ON);
    }

    public ProductResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BaseException());
        return new ProductResponse(product);
    }
}
