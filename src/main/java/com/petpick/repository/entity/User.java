package com.petpick.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {
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

    @Column(name = "user_status")
    private String userStatus;

    @Column(name = "user_img")
    private String userImg;

    @Column(name = "user_googled_id")
    private String userGoogledId;

    @Column(name = "user_refresh_id")
    private String userRefreshId;

    @Column(name = "user_refresh_exp")
    private String userRefreshExp;
}
