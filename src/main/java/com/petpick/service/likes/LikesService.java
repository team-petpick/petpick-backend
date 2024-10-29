package com.petpick.service.likes;

import com.petpick.domain.Likes;
import com.petpick.domain.Product;
import com.petpick.domain.User;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.global.exception.errorCode.UserErrorCode;
import com.petpick.repository.LikesRepository;
import com.petpick.repository.ProductRepository;
import com.petpick.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addLike(Integer productId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Product product = getProductById(productId);

        Likes likes = new Likes(user, product);
        likesRepository.save(likes);
    }

    @Transactional
    public void removeLike(Integer productId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Product product = getProductById(productId);

        likesRepository.deleteByUserAndProduct(user, product);
    }

    private User getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BaseException(UserErrorCode.EMPTY_MEMBER));
    }

    private Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

}
