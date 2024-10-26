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
}
