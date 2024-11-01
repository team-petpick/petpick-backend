package com.petpick.domain;

import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer cartId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @Column(name = "cart_cnt")
    private Integer cartCnt;

}
