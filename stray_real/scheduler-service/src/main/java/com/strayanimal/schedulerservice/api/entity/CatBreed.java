package com.strayanimal.schedulerservice.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cat_breeds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String length;

    private String origin;

    private Double minLifeExpectancy;

    private Double maxLifeExpectancy;

    private Double minWeight;

    private Double maxWeight;

    private Integer familyFriendly;

    private Integer shedding;

    private Integer generalHealth;

    private Integer playfulness;

    private Integer childrenFriendly;

    private Integer grooming;

    private Integer intelligence;

    private Integer otherPetsFriendly;
}
