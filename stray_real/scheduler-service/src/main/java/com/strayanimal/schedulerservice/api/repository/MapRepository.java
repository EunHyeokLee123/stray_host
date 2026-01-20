package com.strayanimal.schedulerservice.api.repository;

import com.strayanimal.schedulerservice.api.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * MapEntity에 대한 DB 접근 인터페이스(JPA Repository)
 * 기본 CRUD 기능 제공 + title 기반 조회/삭제 기능 추가
 */
public interface MapRepository extends JpaRepository<MapEntity, String> {

    /**
     * contentId 기준으로 지도 데이터 조회
     * @param contentId 컨텐츠id
     * @return Optional<MapEntity> - 존재하면 동물 엔티티 반환
     */
    Optional<MapEntity> findByContentId(String contentId);
}