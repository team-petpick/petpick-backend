package com.petpick.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_img")
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id")
    private Integer productImgId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_img_url")
    private String productImgUrl;

    // Business method to create a new instance with updated URL
    public ProductImg withUpdatedUrl(String newUrl) {
        return new ProductImg(this.productImgId, this.product, newUrl);
    }
}
