package com.petpick.repository;

import com.petpick.domain.Category;
import com.petpick.domain.Product;
import com.petpick.domain.type.PetKind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @EntityGraph(attributePaths = {"seller", "category"})
    List<Product> findAll();

    @EntityGraph(attributePaths = {"seller", "category"})
    Page<Product> findByPetKind(PetKind petKind, Pageable pageable);

    @EntityGraph(attributePaths = {"seller", "category"})
    Page<Product> findByPetKindAndCategory(PetKind petKind, Category category, Pageable pageable);


    @EntityGraph(attributePaths = {"seller", "category"})
    Page<Product> findByProductNameContaining(String productName, Pageable pageable);

    @EntityGraph(attributePaths = {"seller", "category"})
    Page<Product> findByPetKindAndProductNameContaining(PetKind petKind, String productName, Pageable pageable);

    @EntityGraph(attributePaths = {"seller", "category"})
    Page<Product> findByPetKindAndCategoryAndProductNameContaining(PetKind petKind, Category category, String productName, Pageable pageable);

//    @Query("SELECT p FROM Product p " +
//            "LEFT JOIN p.likes l " +
//            "GROUP BY p " +
//            "ORDER BY COUNT(l) DESC")
//    Page<Product> findAllOrderByLikesCountDesc(Pageable pageable);

//    // 필터 조건이 있는 경우를 위한 메서드들...
//    @Query("SELECT p FROM Product p " +
//            "LEFT JOIN p.likes l " +
//            "WHERE (:petKind IS NULL OR p.petKind = :petKind) " +
//            "AND (:category IS NULL OR p.category = :category) " +
//            "AND (:search IS NULL OR p.productName LIKE %:search%) " +
//            "GROUP BY p " +
//            "ORDER BY COUNT(l) DESC")
//    Page<Product> findByFilterOrderByLikesCountDesc(
//            @Param("petKind") PetKind petKind,
//            @Param("category") Category category,
//            @Param("search") String search,
//            Pageable pageable);
}
