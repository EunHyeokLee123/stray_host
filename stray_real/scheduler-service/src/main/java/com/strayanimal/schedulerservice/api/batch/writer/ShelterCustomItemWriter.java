package com.strayanimal.schedulerservice.api.batch.writer;

import com.strayanimal.schedulerservice.api.entity.PetShelter;
import com.strayanimal.schedulerservice.api.entity.StrayAnimalEntity;
import com.strayanimal.schedulerservice.api.repository.ShelterRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ShelterCustomItemWriter implements ItemWriter<PetShelter> {

    private final ShelterRepository shelterRepository;

    // 이번 배치에서 API로 수집한 모든 유기번호를 모아놓는 Set
    // 이후 "DB에는 있는데, API에는 없는 데이터"를 삭제할 때 사용할 수 있음
    @Getter
    private final Set<String> careRegNoFromApi = new HashSet<>();

    @Override
    @Transactional  // 하나의 Chunk 안에서 모든 쓰기 작업이 트랜잭션으로 처리됨
    public void write(Chunk<? extends PetShelter> items) {
        for (PetShelter incoming : items) {

            // 유기번호를 Set에 저장 (삭제 대상 판별용)
            careRegNoFromApi.add(incoming.getCareRegNo());

            // DB에서 같은 유기번호가 이미 존재하는지 확인
            Optional<PetShelter> optional = shelterRepository.findByCareRegNo(incoming.getCareRegNo());

            // 이미 존재하는 경우 → 데이터가 바뀌었는지 확인 후 업데이트
            // 이미 존재하는 경우 → 데이터가 바뀌었는지 확인 후 업데이트
            if (optional.isPresent()) {
                PetShelter existing = optional.get();

                // 비교해서 기존 데이터와 다른 경우만 업데이트
                if (isChanged(existing, incoming)) {
                    existing.updateIfChanged(incoming);  // 변경된 필드만 업데이트
                    shelterRepository.save(existing);    // DB 저장
                }

            } else {
                // DB에 존재하지 않는 유기번호 → 신규 데이터로 저장
                shelterRepository.save(incoming);
            }
        }
    }

    // 일단 전부 다 비교하자.
    private boolean isChanged(PetShelter db, PetShelter incoming) {
        return !Objects.equals(db.getDataStdDt(), incoming.getDataStdDt())
                || !Objects.equals(db.getCareNm(), incoming.getCareNm())
                || !Objects.equals(db.getOrgNm(), incoming.getOrgNm())
                || !Objects.equals(db.getDivisionNm(), incoming.getDivisionNm())
                || !Objects.equals(db.getSaveTrgtAnimal(), incoming.getSaveTrgtAnimal())
                || !Objects.equals(db.getCareAddr(), incoming.getCareAddr())
                || !Objects.equals(db.getJibunAddr(), incoming.getJibunAddr())
                || !Objects.equals(db.getLat(), incoming.getLat())
                || !Objects.equals(db.getLng(), incoming.getLng())
                || !Objects.equals(db.getDsignationDate(), incoming.getDsignationDate())
                || !Objects.equals(db.getWeekOprStime(), incoming.getWeekOprStime())
                || !Objects.equals(db.getWeekOprEtime(), incoming.getWeekOprEtime())
                || !Objects.equals(db.getWeekCellStime(), incoming.getWeekCellStime())
                || !Objects.equals(db.getWeekCellEtime(), incoming.getWeekCellEtime())
                || !Objects.equals(db.getWeekendOprStime(), incoming.getWeekendOprStime())
                || !Objects.equals(db.getWeekendOprEtime(), incoming.getWeekendOprEtime())
                || !Objects.equals(db.getWeekendCellStime(), incoming.getWeekendCellStime())
                || !Objects.equals(db.getWeekendCellEtime(), incoming.getWeekendCellEtime())
                || !Objects.equals(db.getCloseDay(), incoming.getCloseDay())
                || !Objects.equals(db.getVetPersonCnt(), incoming.getVetPersonCnt())
                || !Objects.equals(db.getSpecsPersonCnt(), incoming.getSpecsPersonCnt())
                || !Objects.equals(db.getMedicalCnt(), incoming.getMedicalCnt())
                || !Objects.equals(db.getBreedCnt(), incoming.getBreedCnt())
                || !Objects.equals(db.getQuarabtineCnt(), incoming.getQuarabtineCnt())
                || !Objects.equals(db.getFeedCnt(), incoming.getFeedCnt())
                || !Objects.equals(db.getTransCarCnt(), incoming.getTransCarCnt())
                || !Objects.equals(db.getCareTel(), incoming.getCareTel());
    }

}
