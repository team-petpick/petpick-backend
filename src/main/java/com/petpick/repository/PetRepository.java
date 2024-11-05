package com.petpick.repository;

import com.petpick.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * findByUserId is to search all the animals that the user has
 * findByPetName is to find pet by pet's name
 */
public interface PetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findByUser_UserId(Integer userId);
    List<Pet> findByPetName(String petName);

}
