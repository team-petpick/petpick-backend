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
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addLike(Integer productId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Product product = getProductById(productId);

        Optional<Likes> likes = likesRepository.findByUserAndProduct(user, product);
        if (likes.isPresent()) {
            throw new BaseException(LikesErrorCode.ALREADY_LIKE_PRODUCT);
        }

        Likes likesEntity = new Likes(user, product);
        likesRepository.save(likesEntity);
    }

    @Transactional
    public void removeLike(Integer productId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Product product = getProductById(productId);

        Optional<Likes> likes = likesRepository.findByUserAndProduct(user, product);
        if (!likes.isPresent()) {
            throw new BaseException(LikesErrorCode.ALREADY_UNLIKE_PRODUCT);
        }

        likesRepository.delete(likes.get());
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
