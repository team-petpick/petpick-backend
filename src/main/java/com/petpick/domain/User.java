package com.petpick.domain;

import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private String userNickname;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String userImg;

    private String userRefreshToken;

    public void changeUserStatus(UserStatus newStatus) {
        this.userStatus = newStatus;
    }

    public void updateRefreshToken(String refreshToken) {
        this.userRefreshToken = refreshToken;
    }
}
