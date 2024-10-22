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

    @Column(name = "user_name", length = 20)
    private String userName;

    @Column(name = "user_nickname", length = 20)
    private String userNickname;

    @Column(name = "user_email", length = 100)
    private String userEmail;

    @Column(name = "user_status", length = 20)
    private String userStatus;

    @Column(name = "user_img", columnDefinition = "TEXT")
    private String userImg;

    @Column(name = "user_googled_id", length = 30)
    private String userGoogledId;
}
