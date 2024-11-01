package com.petpick.repository;

import com.petpick.domain.Cart;
import com.petpick.domain.Product;
import com.petpick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserUserId(Integer userId);
    Optional<Cart> findByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
    void deleteByUserUserId(Integer userId);
}
