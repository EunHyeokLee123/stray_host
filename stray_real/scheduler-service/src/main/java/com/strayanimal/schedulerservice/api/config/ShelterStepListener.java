package com.strayanimal.schedulerservice.api.config;

import com.strayanimal.schedulerservice.api.batch.writer.ShelterCustomItemWriter;
import com.strayanimal.schedulerservice.api.entity.PetShelter;
import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import com.strayanimal.schedulerservice.api.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ShelterStepListener implements StepExecutionListener {

    private final ShelterRepository shelterRepository;

    private final ShelterCustomItemWriter itemWriter;

    /**
     * Step 실행 이후에 호출되는 메서드.
     * Step 내부에서 처리된 결과를 바탕으로 후처리 작업을 수행할 수 있다.
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        // Step 실행 중에 수집된 모든 유기번호 (API 응답 기준)
        Set<String> desertionNoFromApi = itemWriter.getCareRegNoFromApi();

        // 현재 DB에 저장되어 있는 모든 유기동물 데이터를 조회
        List<PetShelter> allInDb = shelterRepository.findAll();

        // 전체 DB 데이터를 순회하면서 삭제 조건에 해당하는지 검사
        for (PetShelter entity : allInDb) {

            // API 결과에 포함되지 않은 유기번호
            boolean notInApi = !desertionNoFromApi.contains(entity.getCareRegNo());

            if (notInApi) {
                shelterRepository.delete(entity);
            }
        }

        // 특별한 종료 상태는 없음 (null 반환 → 기본 처리)
        return null;
    }

}
