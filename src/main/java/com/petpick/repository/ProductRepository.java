package com.petpick.repository;

import com.petpick.domain.Product;
import com.petpick.domain.type.PetKind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @EntityGraph(attributePaths = {"seller", "category"})
    List<Product> findAll();

    Page<Product> findByPetKind(PetKind petKind, Pageable pageable);
}
