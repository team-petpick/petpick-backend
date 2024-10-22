package com.petpick.domain;

import com.petpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private String userNickname;

    private String userEmail;

    private String userStatus;

    private String userImg;

    private String userGoogleid;
}
