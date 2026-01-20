package com.strayanimal.mapservice.map.repository;

import com.strayanimal.mapservice.map.entity.FestivalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FestivalRepository extends JpaRepository<FestivalEntity, String> {

    // addr이 없는 값은 뽑지 않음.
    @Query("SELECT f FROM FestivalEntity f WHERE f.addr is not null")
    Page<FestivalEntity> findValidList(Pageable pageable);

    @Query("SELECT f FROM FestivalEntity f WHERE f.festivalId = :id")
    Optional<FestivalEntity> findId(@Param("id") Long id);

}
