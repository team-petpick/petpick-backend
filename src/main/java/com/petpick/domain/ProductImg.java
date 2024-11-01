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

    @Column(name = "product_image_thumbnail")
    private Integer productImgThumb;

    @Column(name = "product_img_url")
    private String productImgUrl;

    @Column(name = "desc_img_status")
    private Integer descImgStatus;

    // Corrected method to include both thumbnail and description flags
    public ProductImg withUpdatedFields(String newUrl, Integer newThumb, Integer newDescImgStatus) {
        return new ProductImg(this.productImgId, this.product, newThumb, newUrl, newDescImgStatus);
    }
}
