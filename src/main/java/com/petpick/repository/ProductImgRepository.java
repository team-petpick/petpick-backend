package com.petpick.repository;

import com.petpick.domain.Product;
import com.petpick.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    List<ProductImg> findAllByProduct_productId(Integer productId);

    @Query("SELECT pi.productImgUrl FROM ProductImg pi WHERE pi.product.productId = :productId AND pi.productImgThumb = 1")
    String findThumbnailByProductId(Integer productId);

    List<ProductImg> findByProductAndProductImgThumb(Product product, Integer productImgThumb);

    List<ProductImg> findByProductAndDescImgStatus(Product product, Integer descImgStatus);
}
