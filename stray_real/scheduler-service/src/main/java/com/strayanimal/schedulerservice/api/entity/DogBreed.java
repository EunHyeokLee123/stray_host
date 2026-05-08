package com.strayanimal.schedulerservice.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "dog_breeds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogBreed {

    @Id
    @Column(name = "breed_key", nullable = false, length = 120)
    private String key;

    @Column(nullable = false)
    private String name;

    private String imageLink;

    private Integer energy;
    private Integer trainability;
    private Integer protectiveness;
    private Integer shedding;
    private Integer barking;
    private Integer playfulness;
    private Integer grooming;
    private Integer drooling;
    private Integer coatLength;

    private Integer goodWithOtherDogs;
    private Integer goodWithStrangers;

    private Integer minLifeExpectancy;
    private Integer maxLifeExpectancy;

    private Double minHeightMale;
    private Double maxHeightMale;
    private Double minHeightFemale;
    private Double maxHeightFemale;

    private Double minWeightMale;
    private Double maxWeightMale;
    private Double minWeightFemale;
    private Double maxWeightFemale;

    private Integer lifeExpectancy;

    @Column(columnDefinition = "TEXT")
    private String origin;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String temperament;

    @Column(columnDefinition = "TEXT")
    private String colors;

    private String sizeCategory;
    private Integer popularityScore;
    private String breedGroup;
    private String breedFunction;
    private String coatType;

    private Integer childrenFriendly;
    private Integer apartmentFriendly;
    private Integer noviceOwnerFriendly;
    private Integer preyDrive;
    private Integer exerciseNeeds;
    private Integer heatTolerance;
    private Integer coldTolerance;

    private String reviewStatus;
    private String sourceRegistry;

    @Column(columnDefinition = "TEXT")
    private String sourceUrl;

    private LocalDate reviewedAt;
}