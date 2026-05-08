package com.strayanimal.schedulerservice.api.repository;

import com.strayanimal.schedulerservice.api.entity.Cat_proc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CatRepository extends JpaRepository<Cat_proc, String> {

    @Query("SELECT c FROM Cat_proc c WHERE c.desertion_no = :no")
    Optional<Cat_proc> findByDe(@Param("no") String desertionNo);

}
