package com.strayanimal.schedulerservice.api.util;

import com.strayanimal.schedulerservice.api.entity.CatBreed;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


public class CatBreedFileSetMapper implements FieldSetMapper<CatBreed> {

    @Override
    public CatBreed mapFieldSet(FieldSet fieldSet) throws BindException {
        CatBreed catBreed = new CatBreed();

        catBreed.setName(fieldSet.readString("name"));
        catBreed.setLength(fieldSet.readString("length"));
        catBreed.setOrigin(fieldSet.readString("origin"));

        catBreed.setMinLifeExpectancy(readDouble(fieldSet, "min_life_expectancy"));
        catBreed.setMaxLifeExpectancy(readDouble(fieldSet, "max_life_expectancy"));

        catBreed.setMinWeight(readDouble(fieldSet, "min_weight"));
        catBreed.setMaxWeight(readDouble(fieldSet, "max_weight"));

        catBreed.setFamilyFriendly(readInt(fieldSet, "family_friendly"));
        catBreed.setShedding(readInt(fieldSet, "shedding"));
        catBreed.setGeneralHealth(readInt(fieldSet, "general_health"));
        catBreed.setPlayfulness(readInt(fieldSet, "playfulness"));
        catBreed.setChildrenFriendly(readInt(fieldSet, "children_friendly"));
        catBreed.setGrooming(readInt(fieldSet, "grooming"));
        catBreed.setIntelligence(readInt(fieldSet, "intelligence"));
        catBreed.setOtherPetsFriendly(readInt(fieldSet, "other_pets_friendly"));

        return catBreed;
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

}
