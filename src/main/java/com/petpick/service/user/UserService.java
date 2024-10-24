package com.petpick.service.user;

import com.petpick.domain.User;
import com.petpick.domain.UserStatus;
import com.petpick.model.GoogleUserInfoResponse;
import com.petpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}