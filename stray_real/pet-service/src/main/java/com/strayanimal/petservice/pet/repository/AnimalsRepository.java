package com.strayanimal.petservice.pet.repository;

import com.strayanimal.petservice.pet.dto.SearchDto;
import com.strayanimal.petservice.pet.entity.StrayAnimalEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * AnimalsEntity에 대한 DB 접근 인터페이스(JPA Repository)
 * 기본 CRUD 기능 제공 + desertionNo 기반 조회/삭제 기능 추가
 */
public interface AnimalsRepository extends JpaRepository<StrayAnimalEntity, String> {

    /**
     * desertionNo를 기준으로 동물 데이터 조회
     * @param desertionNo 유기번호 (유니크)
     * @return Optional<AnimalsEntity> - 존재하면 동물 엔티티 반환
     */
    Optional<StrayAnimalEntity> findByDesertionNo(String desertionNo);

    @Query("SELECT s FROM StrayAnimalEntity s WHERE s.upKindNm = :kind " +
            "AND SUBSTRING(s.careAddr, 1, LOCATE(' ', s.careAddr) - 1) = :region ")
    Page<StrayAnimalEntity> searchList(@Param("region") String region, @Param("kind") String kind,
                                       Pageable pageable);

    // 전북인 경우
    @Query("SELECT s FROM StrayAnimalEntity s WHERE s.upKindNm = :kind " +
            "AND SUBSTRING(s.careAddr, 1, LOCATE(' ', s.careAddr) - 1) IN (:region1, :region2)")
    Page<StrayAnimalEntity> searchList2(@Param("region1") String region1, @Param("region2") String region2,
                                        @Param("kind") String kind, Pageable pageable);

    @Query("SELECT s FROM StrayAnimalEntity s WHERE s.upKindNm = :kind")
    Page<StrayAnimalEntity> searchAll(@Param("kind") String kind, Pageable pageable);
}