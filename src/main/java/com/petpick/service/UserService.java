package com.petpick.service;

import com.petpick.domain.User;
import com.petpick.model.GoogleUserInfoResponse;
import com.petpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findOrCreateUser(GoogleUserInfoResponse googleUserInfoResponse) {

    }
}
