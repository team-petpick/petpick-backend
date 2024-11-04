package com.petpick.service.likes;

import com.petpick.domain.Likes;
import com.petpick.domain.Product;
import com.petpick.domain.User;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.LikesErrorCode;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.global.exception.errorCode.UserErrorCode;
import com.petpick.repository.LikesRepository;
import com.petpick.repository.ProductRepository;
import com.petpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public boolean toggleLike(Integer productId, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<Likes> existingLike = likesRepository.findByUserAndProduct(user, product);

        if (existingLike.isPresent()) {
            // 좋아요가 이미 존재하면 삭제
            likesRepository.delete(existingLike.get());
            return false;
        } else {
            // 좋아요가 없으면 추가
            Likes like = new Likes(user, product);
            likesRepository.save(like);
            return true;
        }
    }

}
