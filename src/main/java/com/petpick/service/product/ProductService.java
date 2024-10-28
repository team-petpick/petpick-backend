package com.petpick.service.product;

import com.petpick.domain.Category;
import com.petpick.domain.Product;
import com.petpick.domain.type.PetKind;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.model.ProductDetailResponse;
import com.petpick.model.ProductListResponse;
import com.petpick.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public Page<ProductListResponse> getProductsList(
            String productType,
            Integer categoryId,
            Integer page,
            Integer size,
            String sort
    ) {
        Sort sortOrder = Sort.by("createAt").descending(); // 기본 정렬

        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split("_");
            String sortBy = sortParams[0];
            String direction = sortParams.length > 1 ? sortParams[1] : "desc";

            sortOrder = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if(pageable.isPaged()){
            throw new BaseException(ProductErrorCode.INVALID_PAGE_PARAMETER);
        }

        // 필터링 조건 설정
        PetKind petKind = null;
        if (productType != null && !productType.isEmpty()) {
            petKind = PetKind.valueOf(productType.toUpperCase());
        }

        if(petKind == null){
            throw new BaseException(ProductErrorCode.INVALID_TYPE_VALUE);
        }

        /*
        * 전체 -> 상품 타입 -> 카테고리 순이기 때문에 카테고리만으로 필터링하는 로직은 X
        * */
        Page<Product> productsPage;
        if (petKind != null && categoryId != null) {
            Category category = categoryRepository.findByCategoryId(categoryId);
            if(category == null){
                throw new BaseException(ProductErrorCode.INVALID_CATEGORY_VALUE);
            }
            productsPage = productRepository.findByPetKindAndCategory(petKind, category, pageable);
        } else if (petKind != null) {
            productsPage = productRepository.findByPetKind(petKind, pageable);
        } else {
            productsPage = productRepository.findAll(pageable);
        }

        return productsPage.map(ProductListResponse::new);
    }

    public ProductDetailResponse getProductById(Integer id) { // =product id
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BaseException(ProductErrorCode.PRODUCT_NOT_FOUND));
        int likesCount = likesRepository.countByProduct_ProductId(id);

        return new ProductDetailResponse(product, likesCount);
    }

}
