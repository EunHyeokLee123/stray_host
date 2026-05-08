package com.strayanimal.schedulerservice.api.repository;

import com.strayanimal.schedulerservice.api.entity.PetShelter;
import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShelterRepository extends JpaRepository<PetShelter, String> {


    Optional<PetShelter> findByCareRegNo(String desertionNo);

    @Query(value = """
SELECT * FROM pet_shelter WHERE care_nm = :name LIMIT 1
""", nativeQuery = true)
    Optional<PetShelter> findByName(@Param("name") String careNm);

}
