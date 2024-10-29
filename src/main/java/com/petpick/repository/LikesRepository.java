package com.petpick.repository;

import com.petpick.domain.Likes;
import com.petpick.domain.Product;
import com.petpick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Integer> {
    // search from product id
    Integer countByProduct_ProductId(Integer productId);

    Likes deleteByUserAndProduct(User user, Product product);
}
