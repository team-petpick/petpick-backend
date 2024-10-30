package com.petpick.domain;

import com.petpick.domain.type.PetGender;
import com.petpick.domain.type.PetKind;
import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class Pet extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer petId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "pet_species")
    private String petSpecies;


    @Enumerated(EnumType.STRING)
    @Column(name = "pet_kind")
    private PetKind petKind;


    @Column(name = "pet_img")
    private String petImg;

    @Column(name = "pet_age")
    private Integer petAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_gender")
    private PetGender petGender;

    public Pet withUpdatedFields(String petName, String petSpecies, PetKind petKind, Integer petAge,
                                 PetGender petGender, String petImg) {
        return new Pet(this.petId, this.user, petName, petSpecies, petKind, petImg, petAge, petGender);
    }
}
