package com.petpick.repository;

import com.petpick.domain.Category;
import com.petpick.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
}
