package com.petpick.repository;

import com.petpick.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @EntityGraph(attributePaths = {"seller", "category"})
    List<Product> findAll();
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
}
