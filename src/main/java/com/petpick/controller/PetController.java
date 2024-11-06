package com.petpick.controller;

import com.petpick.model.PetInfoResponse;
import com.petpick.service.pet.PetService;
import com.petpick.domain.type.PetGender;
import com.petpick.domain.type.PetKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/pets")
public class PetController {

    @Autowired
    private PetService petService;

    // Create a new pet
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PetInfoResponse createPet(
            @RequestAttribute Integer userId,
            @RequestParam("petName") String petName,
            @RequestParam("petSpecies") String petSpecies,
            @RequestParam("petKind") PetKind petKind,
            @RequestParam(value = "petAge", required = false) Integer petAge,
            @RequestParam(value = "petGender", required = false) PetGender petGender,
            @RequestParam(value = "petImg", required = false) MultipartFile petImg) throws IOException {

        return petService.createPet(userId, petName, petSpecies, petKind, petAge, petGender, petImg);
    }

    // Get pet by ID
    @GetMapping
    public PetInfoResponse getPet(
            @RequestAttribute Integer userId) {
        return petService.getPetById(userId).getBody();
    }

//    // Get all pets
//    @GetMapping
//    public List<PetInfoResponse> getAllPets() {
//        return petService.getAllPets();
//    }

    // Update pet
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PetInfoResponse updatePet(
            @RequestAttribute Integer userId,
            @RequestParam(value = "petName", required = false) String petName,
            @RequestParam(value = "petSpecies", required = false) String petSpecies,
            @RequestParam(value = "petKind", required = false) PetKind petKind,
            @RequestParam(value = "petAge", required = false) Integer petAge,
            @RequestParam(value = "petGender", required = false) PetGender petGender,
            @RequestParam(value = "petImg", required = false) MultipartFile petImg) throws IOException {

        return petService.updatePet(userId, petName, petSpecies, petKind, petAge, petGender, petImg);
    }

    // Delete pet
    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable Integer petId) {
        petService.deletePet(petId);
    }

    // Update pet image only
    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PetInfoResponse updatePetImage(
            @RequestAttribute Integer userId,
            @RequestParam("petImg") MultipartFile petImg) throws IOException {
        System.out.println("유저스: " + userId);
        return petService.updatePetImage(userId, petImg);
    }

    // Delete pet image only
    @DeleteMapping("/{petId}/image")
    public void deletePetImage(@PathVariable Integer petId) {
        petService.deletePetImage(petId);
    }
}
