package com.petpick.service.product;

import com.petpick.domain.Product;
import com.petpick.domain.type.ProductStatus;
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
}
