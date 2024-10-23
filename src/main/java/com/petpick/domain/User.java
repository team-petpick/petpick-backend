package com.petpick.domain;

import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

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

    private String userStatus;

    private String userImg;

    private String userGoogleId;

    private String userRefreshId;

    private Date userRefreshExpire;
}
