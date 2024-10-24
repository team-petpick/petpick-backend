package com.petpick.domain;

import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Column(name = "user_img")
    private String userImg;

    @Column(name = "user_refresh_token")
    private String userRefreshToken;

    @Column(name = "user_position")
    private String userPosition;

    public void changeUserStatus(UserStatus newStatus) {
        this.userStatus = newStatus;
    }

    public void updateRefreshToken(String refreshToken) {
        this.userRefreshToken = refreshToken;
    }
}
