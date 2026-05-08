package com.strayanimal.schedulerservice.api.repository;

import com.strayanimal.schedulerservice.api.entity.Cat_proc;
import com.strayanimal.schedulerservice.api.entity.Dog_proc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DogRepository extends JpaRepository<Dog_proc, String> {

    @Query("SELECT d FROM Dog_proc d WHERE d.desertion_no = :no")
    Optional<Dog_proc> findByDe(@Param("no") String desertionNo);

}
