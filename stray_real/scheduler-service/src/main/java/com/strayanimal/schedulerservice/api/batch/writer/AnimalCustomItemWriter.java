package com.strayanimal.schedulerservice.api.batch.writer;

import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import com.strayanimal.schedulerservice.api.repository.AnimalsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Spring Batch에서 데이터를 DB에 기록하는 역할을 하는 ItemWriter 구현체.

 * - AnimalApiItemReader로부터 넘어온 AnimalsEntity들을 DB에 저장 또는 업데이트.
 * - 이미 존재하는 유기번호(desertionNo)의 경우, 변경된 값만 업데이트.
 * - 처음 보는 유기번호는 신규 데이터로 저장.
 * - 유기번호를 Set에 저장해서 이후 삭제용 필터링에도 활용 가능.
 */
@Component
@RequiredArgsConstructor
public class AnimalCustomItemWriter implements ItemWriter<StrayAnimalEntity> {

    // Spring Data JPA를 통한 DB 접근용 Repository
    private final AnimalsRepository animalsRepository;

    @PersistenceContext
    private final EntityManager em; // 영속성 컨텍스트 직접 접근

    private static final int DELETE_CHUNK_SIZE = 500; // 한 번에 삭제할 청크 크기
    private static final int INSERT_CHUNK_SIZE = 150; // 한 번에 삽입할 청크 크기

    /**
     * 모든 기존 데이터를 삭제하고, 새 데이터를 삽입
     */
    @Override
    public void write(Chunk<? extends StrayAnimalEntity> items) throws Exception {
        if (items.isEmpty()) {
            return;
        }

        /*// 1. DB 전체 삭제를 청크 단위로 수행
        long totalCount = animalsRepository.count();
        for (int i = 0; i < totalCount; i += DELETE_CHUNK_SIZE) {
            List<StrayAnimalEntity> chunk = animalsRepository
                    .findAll(PageRequest.of(i / DELETE_CHUNK_SIZE, DELETE_CHUNK_SIZE))
                    .getContent();
            if (!chunk.isEmpty()) {
                animalsRepository.deleteAll(chunk);
                animalsRepository.flush(); // 즉시 DB 반영
            }
        }*/

        animalsRepository.truncateTable();

        // 2. 새 데이터 삽입 (청크 단위)
        List<? extends StrayAnimalEntity> itemList = items.getItems();
        int total = itemList.size();
        for (int i = 0; i < total; i += INSERT_CHUNK_SIZE) {
            int end = Math.min(i + INSERT_CHUNK_SIZE, total);
            List<? extends StrayAnimalEntity> subList = itemList.subList(i, end);

            // 서브 청크 삽입
            animalsRepository.saveAll(subList);

            // flush & clear로 Hibernate 세션 최적화
            animalsRepository.flush();
            em.clear();
        }

    }
}