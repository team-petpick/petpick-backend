package com.petpick.repository;

import com.petpick.domain.Likes;
import com.petpick.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    // search from product id
    Integer countByProduct_ProductId(Integer productId);
}
