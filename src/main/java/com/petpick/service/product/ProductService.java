package com.petpick.service.product;

import com.petpick.domain.Category;
import com.petpick.domain.Product;
import com.petpick.domain.ProductImg;
import com.petpick.domain.type.PetKind;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.model.ProductDetailResponse;
import com.petpick.model.ProductListResponse;
import com.petpick.repository.CategoryRepository;
import com.petpick.repository.LikesRepository;
import com.petpick.repository.ProductImgRepository;
import com.petpick.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final LikesRepository likesRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductListResponse> getProductsList(
            String productType,
            Integer categoryId,
            Integer page,
            Integer size,
            String sort,
            String search
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

        if(pageable.isUnpaged()){
            throw new BaseException(ProductErrorCode.INVALID_PAGE_PARAMETER);
        }

        // 필터링 조건 설정
        PetKind petKind = null;
        if (productType != null && !productType.isEmpty()) {
            try {
                petKind = PetKind.valueOf(productType.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BaseException(ProductErrorCode.INVALID_TYPE_VALUE);
            }
        }

        /*
         * 전체 -> 상품 타입 -> 카테고리 순이기 때문에 카테고리만으로 필터링하는 로직은 X
         * */
        Page<Product> productsPage;
        if(search == null || search.isEmpty()) {
            System.out.println("Empty is Entered");
            if (petKind == null) {
                productsPage = productRepository.findAll(pageable);
            } else {
                if (categoryId == null) {
                    productsPage = productRepository.findByPetKind(petKind, pageable);
                } else {
                    Category category = categoryRepository.findByCategoryId(categoryId);
                    if (category == null) {
                        throw new BaseException(ProductErrorCode.INVALID_CATEGORY_VALUE);
                    }
                    productsPage = productRepository.findByPetKindAndCategory(petKind, category, pageable);
                }
            }
        } else {
            if (petKind == null) {
                if (categoryId == null) {
                    productsPage = productRepository.findByProductNameContaining(search, pageable);
                } else {
                    // 카테고리만으로 필터링하지 않으므로 예외 처리
                    throw new BaseException(ProductErrorCode.INVALID_CATEGORY_VALUE);
                }
            } else {
                if (categoryId == null) {
                    productsPage = productRepository.findByPetKindAndProductNameContaining(petKind, search, pageable);
                } else {
                    Category category = categoryRepository.findByCategoryId(categoryId);
                    if (category == null) {
                        throw new BaseException(ProductErrorCode.INVALID_CATEGORY_VALUE);
                    }
                    productsPage = productRepository.findByPetKindAndCategoryAndProductNameContaining(petKind, category, search, pageable);
                }
            }
        }

        if(!productsPage.hasContent()){
            throw new BaseException(ProductErrorCode.NO_PRODUCTS_AVAILABLE);
        }

        Page<ProductListResponse> productListResponses = productsPage.map(product -> {
            Integer productId = product.getProductId();
            String thumbnailUrl = productImgRepository.findThumbnailByProductId(productId);
            return new ProductListResponse(product, thumbnailUrl);
        });

        return productListResponses;
    }

    public ProductDetailResponse getProductById(Integer id) { // =product id
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BaseException(ProductErrorCode.PRODUCT_NOT_FOUND));
        int likesCount = likesRepository.countByProduct_ProductId(id);

        List<ProductImg> productImgs = productImgRepository.findAllByProduct_productId(id);

        return new ProductDetailResponse(product, productImgs, likesCount);
    }


}
