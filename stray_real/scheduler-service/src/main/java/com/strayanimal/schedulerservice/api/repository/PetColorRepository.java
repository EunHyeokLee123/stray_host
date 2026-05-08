package com.strayanimal.schedulerservice.api.repository;

import com.strayanimal.schedulerservice.api.entity.PetColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PetColorRepository extends JpaRepository<PetColor, Long> {

    @Query("SELECT p FROM PetColor p WHERE p.desertionNo = :no")
    Optional<PetColor> findByDesertionNo(@Param("no") String desertionNo);

}
