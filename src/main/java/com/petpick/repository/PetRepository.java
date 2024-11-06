package com.petpick.repository;

import com.petpick.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * findByUserId is to search all the animals that the user has
 * findByPetName is to find pet by pet's name
 */
public interface PetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findByUserUserId(Integer userId);
    @Modifying
    @Transactional
    @Query("UPDATE Pet p SET p.petImg = :petImg WHERE p.user.userId = :userId")
    int updatePetImageByUserId(@Param("userId") Integer userId, @Param("petImg") String petImg);
    List<Pet> findByPetName(String petName);

}
