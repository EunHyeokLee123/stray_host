package com.strayanimal.schedulerservice.api.config;

import com.strayanimal.schedulerservice.api.batch.writer.AnimalCustomItemWriter;
import com.strayanimal.schedulerservice.api.entity.Cat_proc;
import com.strayanimal.schedulerservice.api.entity.Dog_proc;
import com.strayanimal.schedulerservice.api.entity.PetColor;
import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import com.strayanimal.schedulerservice.api.repository.AnimalsRepository;
import com.strayanimal.schedulerservice.api.repository.CatRepository;
import com.strayanimal.schedulerservice.api.repository.DogRepository;
import com.strayanimal.schedulerservice.api.repository.PetColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * [Spring Batch] StepExecutionListener 구현 클래스.
 * - 한 Step의 실행 전/후에 추가 작업을 삽입하고 싶을 때 사용하는 리스너이다.

 * 본 클래스에서는 Step 실행 "후"(`afterStep`)에 아래의 동작을 수행:

 * 주요 기능:
 * 1. 이번 Step에서 API로부터 수집한 모든 유기번호(desertionNo)를 가져온다.
 * 2. DB에 있는 모든 데이터를 조회한다.
 * 3. 다음 조건을 모두 만족하는 데이터는 DB에서 삭제한다:
 *    - 이번 API 호출 결과에 포함되지 않았고
 *    - 해당 동물의 processState가 "보호중"이 아닐 경우

 *    더 이상 보호소에서 관리하고 있지 않은 유기동물이며,
 *    "보호중"도 아닌 경우는 데이터에서 제거하여 정합성을 유지한다.
 */
@Component
@RequiredArgsConstructor
public class AnimalStepListener implements StepExecutionListener {

    // DB 접근용 JPA Repository (전체 조회 및 삭제를 수행)
    private final AnimalsRepository animalsRepository;
    private final PetColorRepository petColorRepository;

    // 이번 Step 동안 API로부터 수집한 유기번호 리스트를 담고 있는 Writer
    private final AnimalCustomItemWriter itemWriter;

    // python 작업을 위한 Job 테이블
    private final DogRepository dogRepository;
    private final CatRepository catRepository;

    @Value("${spring.profiles.active}")
    private String now;

    /**
     * Step 실행 이후에 호출되는 메서드.
     * Step 내부에서 처리된 결과를 바탕으로 후처리 작업을 수행할 수 있다.
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        // Step 실행 중에 수집된 모든 유기번호 (API 응답 기준)
        Set<String> desertionNoFromApi = itemWriter.getDesertionNoFromApi();

        // 현재 DB에 저장되어 있는 모든 유기동물 데이터를 조회
        List<StrayAnimalEntity> allInDb = animalsRepository.findAll();

        // 전체 DB 데이터를 순회하면서 삭제 조건에 해당하는지 검사
        for (StrayAnimalEntity entity : allInDb) {

            // API 결과에 포함되지 않은 유기번호
            boolean notInApi = !desertionNoFromApi.contains(entity.getDesertionNo());

            if (notInApi) {
                animalsRepository.delete(entity);
                // 삭제된 유기동물 데이터면
                // 색상정보도 삭제함.
                Optional<PetColor> found
                        = petColorRepository.findByDesertionNo(entity.getDesertionNo());
                found.ifPresent(petColorRepository::delete);
                if(now.equals("default")) {
                    if (entity.getUpKindNm().equals("개")) {
                        Optional<Dog_proc> fd = dogRepository.findByDe(entity.getDesertionNo());
                        if (fd.isPresent()) {
                            dogRepository.delete(fd.get());
                            dogRepository.save(new Dog_proc(
                                    fd.get().getDesertion_no(), "Delete", "Pending"
                            ));
                        }
                    } else if (entity.getUpKindNm().equals("고양이")) {
                        Optional<Cat_proc> fd = catRepository.findByDe(entity.getDesertionNo());
                        if (fd.isPresent()) {
                            catRepository.delete(fd.get());
                            catRepository.save(new Cat_proc(
                                    fd.get().getDesertion_no(), "Delete", "Pending"
                            ));
                        }
                    }
                }
            }
        }

        // 특별한 종료 상태는 없음 (null 반환 → 기본 처리)
        return null;
    }
}