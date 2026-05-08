package com.strayanimal.schedulerservice.api.repository;

import com.strayanimal.schedulerservice.api.entity.CatBreed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatBreedRepository extends JpaRepository<CatBreed, Long> {
}
