package com.strayanimal.mapservice.map.repository;

import com.strayanimal.mapservice.map.entity.PetShelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShelterRepository extends JpaRepository<PetShelter, String> {


    Optional<PetShelter> findByCareRegNo(String desertionNo);

    @Query(value = """
SELECT * FROM pet_shelter WHERE care_nm = :name LIMIT 1
""", nativeQuery = true)
    Optional<PetShelter> findByName(@Param("name") String careNm);
    
    // 나머지
    @Query("SELECT s FROM PetShelter s WHERE SUBSTRING(s.careAddr, 1, LOCATE(' ', s.careAddr) - 1) = :region")
    List<PetShelter> getShelterList(@Param("region") String region);

    // 전라북도, 전북특별자치도
    // 강원도, 강원특별자치도
    // 제주도, 제주특별자치도
    // 세종시, 세족특별자치시
    @Query("SELECT s FROM PetShelter s WHERE SUBSTRING(s.careAddr, 1, LOCATE(' ', s.careAddr) - 1) IN :region")
    List<PetShelter> getShelterList2(@Param("region") List<String> region);

}
