package com.petpick.service.user;

import com.petpick.domain.Likes;
import com.petpick.domain.Product;
import com.petpick.domain.ProductImg;
import com.petpick.domain.User;
import com.petpick.domain.type.UserStatus;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.global.exception.errorCode.UserErrorCode;
import com.petpick.model.GoogleUserInfoResponse;
import com.petpick.model.UserLikesProductListResponse;
import com.petpick.repository.LikesRepository;
import com.petpick.repository.ProductImgRepository;
import com.petpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProductImgRepository productImgRepository;
    private final LikesRepository likesRepository;

    public User findOrCreateUser(GoogleUserInfoResponse googleUserInfoResponse) {

        Optional<User> existingUser = userRepository.findByUserEmail(googleUserInfoResponse.getEmail());

        // if exists, return exist user
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // create new user
        User newUser = User.builder()
                .userName(googleUserInfoResponse.getName())
                .userEmail(googleUserInfoResponse.getEmail())
                .userImg(googleUserInfoResponse.getPicture())
                .userStatus(UserStatus.ACTIVE)
                .build();

        // return new user
        return userRepository.save(newUser);
    }

    public void saveRefreshToken(User user, String refreshToken) {
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public void deleteRefreshToken(User user) {
        user.updateRefreshToken(null);
        userRepository.save(user);
    }

    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepository.findByUserRefreshToken(refreshToken);
    }

    public Optional<User> findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    public Page<UserLikesProductListResponse> getUserLikesProductList(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.EMPTY_MEMBER));

        Sort sort = Sort.by("createAt").descending();

        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Likes> likesPage = likesRepository.findByUser(user, pageable);

        if (!likesPage.hasContent()){
            throw new BaseException(ProductErrorCode.NO_PRODUCTS_AVAILABLE);
        }

        return likesPage.map(like -> {
            Product product = like.getProduct();
            List<ProductImg> productImgs = productImgRepository.findAllByProduct_productId(product.getProductId());
            return new UserLikesProductListResponse(product, productImgs);
        });
    }


}