package com.petpick.repository;

import com.petpick.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
    // search from product id
    Integer countByProduct_ProductId(Integer productId);
}
