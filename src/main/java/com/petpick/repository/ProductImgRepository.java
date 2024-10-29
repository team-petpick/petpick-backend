package com.petpick.repository;

import com.petpick.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    List<ProductImg> findAllByProduct_productId(Integer productId);
}
