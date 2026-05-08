package com.strayanimal.schedulerservice.api.util;

import com.strayanimal.schedulerservice.api.entity.DogBreed;
import com.strayanimal.schedulerservice.api.entity.PetCulture;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DogBreedFileSetMapper implements FieldSetMapper<DogBreed> {

    @Override
    public DogBreed mapFieldSet(FieldSet fieldSet) throws BindException {
        DogBreed dogBreed = new DogBreed();

        dogBreed.setKey(fieldSet.readString("key"));
        dogBreed.setName(fieldSet.readString("name"));
        dogBreed.setImageLink(fieldSet.readString("image_link"));

        dogBreed.setEnergy(readInt(fieldSet, "energy"));
        dogBreed.setTrainability(readInt(fieldSet, "trainability"));
        dogBreed.setProtectiveness(readInt(fieldSet, "protectiveness"));
        dogBreed.setShedding(readInt(fieldSet, "shedding"));
        dogBreed.setBarking(readInt(fieldSet, "barking"));
        dogBreed.setPlayfulness(readInt(fieldSet, "playfulness"));
        dogBreed.setGrooming(readInt(fieldSet, "grooming"));
        dogBreed.setDrooling(readInt(fieldSet, "drooling"));
        dogBreed.setCoatLength(readInt(fieldSet, "coat_length"));

        dogBreed.setGoodWithOtherDogs(readInt(fieldSet, "good_with_other_dogs"));
        dogBreed.setGoodWithStrangers(readInt(fieldSet, "good_with_strangers"));

        dogBreed.setMinLifeExpectancy(readInt(fieldSet, "min_life_expectancy"));
        dogBreed.setMaxLifeExpectancy(readInt(fieldSet, "max_life_expectancy"));

        dogBreed.setMinHeightMale(readDouble(fieldSet, "min_height_male"));
        dogBreed.setMaxHeightMale(readDouble(fieldSet, "max_height_male"));
        dogBreed.setMinHeightFemale(readDouble(fieldSet, "min_height_female"));
        dogBreed.setMaxHeightFemale(readDouble(fieldSet, "max_height_female"));

        dogBreed.setMinWeightMale(readDouble(fieldSet, "min_weight_male"));
        dogBreed.setMaxWeightMale(readDouble(fieldSet, "max_weight_male"));
        dogBreed.setMinWeightFemale(readDouble(fieldSet, "min_weight_female"));
        dogBreed.setMaxWeightFemale(readDouble(fieldSet, "max_weight_female"));

        dogBreed.setLifeExpectancy(readInt(fieldSet, "life_expectancy"));

        dogBreed.setOrigin(fieldSet.readString("origin"));
        dogBreed.setDescription(fieldSet.readString("description"));
        dogBreed.setTemperament(fieldSet.readString("temperament"));
        dogBreed.setColors(fieldSet.readString("colors"));

        dogBreed.setSizeCategory(fieldSet.readString("size_category"));
        dogBreed.setPopularityScore(readInt(fieldSet, "popularity_score"));
        dogBreed.setBreedGroup(fieldSet.readString("breed_group"));
        dogBreed.setBreedFunction(fieldSet.readString("breed_function"));
        dogBreed.setCoatType(fieldSet.readString("coat_type"));

        dogBreed.setChildrenFriendly(readInt(fieldSet, "children_friendly"));
        dogBreed.setApartmentFriendly(readInt(fieldSet, "apartment_friendly"));
        dogBreed.setNoviceOwnerFriendly(readInt(fieldSet, "novice_owner_friendly"));
        dogBreed.setPreyDrive(readInt(fieldSet, "prey_drive"));
        dogBreed.setExerciseNeeds(readInt(fieldSet, "exercise_needs"));
        dogBreed.setHeatTolerance(readInt(fieldSet, "heat_tolerance"));
        dogBreed.setColdTolerance(readInt(fieldSet, "cold_tolerance"));

        dogBreed.setReviewStatus(fieldSet.readString("review_status"));
        dogBreed.setSourceRegistry(fieldSet.readString("source_registry"));
        dogBreed.setSourceUrl(fieldSet.readString("source_url"));

        dogBreed.setReviewedAt(readDate(fieldSet, "reviewed_at"));

        return dogBreed;
    }

    private Integer readInt(FieldSet fieldSet, String name) {
        String value = fieldSet.readString(name);
        if (value == null || value.isBlank()) return null;
        return (int) Double.parseDouble(value);
    }

    private Double readDouble(FieldSet fieldSet, String name) {
        String value = fieldSet.readString(name);
        if (value == null || value.isBlank()) return null;
        return Double.parseDouble(value);
    }

    private LocalDate readDate(FieldSet fieldSet, String name) {
        String value = fieldSet.readString(name);
        if (value == null || value.isBlank()) return null;
        return LocalDate.parse(value);
    }

}
