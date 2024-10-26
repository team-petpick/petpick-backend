package com.petpick.domain;

import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Integer sellerId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "seller_bn")
    private String sellerBn;

    @Column(name = "seller_number")
    private String sellerNumber;

    @Column(name = "seller_store_name")
    private String sellerStoreName;

    @Column(name = "seller_addr")
    private String sellerAddr;

    @Column(name = "seller_addr_detail")
    private String sellerAddrDetail;
}
