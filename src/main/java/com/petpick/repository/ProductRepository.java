package com.petpick.repository;

import com.petpick.domain.Product;
import com.petpick.domain.type.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductStatus(ProductStatus productStatus);

}
