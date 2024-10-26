package com.petpick.model;

import com.petpick.domain.Category;
import lombok.Data;

@Data
public class CategoryResponse {
    private Integer categoryId;
    private String categoryName;

    public CategoryResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }
}
