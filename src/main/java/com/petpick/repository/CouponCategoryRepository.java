package com.petpick.repository;

import com.petpick.domain.CouponCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Integer> {

}
