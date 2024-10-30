package com.petpick.model;

import com.petpick.domain.Pet;
import com.petpick.domain.type.PetGender;
import com.petpick.domain.type.PetKind;
import lombok.Data;

@Data
public class PetInfoResponse {

    private Integer petId;
    private Integer userId;
    private String petName;
    private PetKind petKind;
    private String petImg;
    private Integer petAge;
    private PetGender petGender;

    public PetInfoResponse(Pet pet) {
        this.petId = pet.getPetId();
        this.userId = pet.getUser().getUserId(); // Assuming User has getUserId()
        this.petName = pet.getPetName();
        this.petKind = pet.getPetKind();
        this.petImg = pet.getPetImg();
        this.petAge = pet.getPetAge();
        this.petGender = pet.getPetGender();
    }
}

