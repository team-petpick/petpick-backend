package com.petpick.service.product;

import com.petpick.domain.Product;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.model.ProductDetailResponse;
import com.petpick.model.ProductListResponse;
import com.petpick.repository.LikesRepository;
import com.petpick.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LikesRepository likesRepository;

    public Page<ProductListResponse> getProductsList(Integer page, Integer size, String sort) {
        Sort sortOrder = Sort.by("createAt").descending(); // 기본 정렬 설정

        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split("_");
            String sortBy = sortParams[0];
            String direction = sortParams.length > 1 ? sortParams[1] : "desc";

            sortOrder = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Product> productsPage = productRepository.findAll(pageable);

        return productsPage.map(ProductListResponse::new);
    }

    public ProductDetailResponse getProductById(Integer id) { // =product id
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BaseException(ProductErrorCode.PRODUCT_NOT_FOUND));
        int likesCount = likesRepository.countByProduct_ProductId(id);

        return new ProductDetailResponse(product, likesCount);
    }

}
