package com.petpick.domain;

import com.petpick.domain.type.PetKind;
import com.petpick.domain.type.ProductStatus;
import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_share")
    private Integer productShare;

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_cnt")
    private Integer productCnt;

    @Column(name = "product_sale")
    private Integer productSale;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private PetKind petKind;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;
}