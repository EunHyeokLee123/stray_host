package com.strayanimal.petservice.pet.service;

import com.strayanimal.petservice.common.dto.CommonResDto;
import com.strayanimal.petservice.common.enumeration.ErrorCode;
import com.strayanimal.petservice.common.exception.CommonException;
import com.strayanimal.petservice.pet.dto.SearchDto;
import com.strayanimal.petservice.pet.dto.res.PetDetailResDto;
import com.strayanimal.petservice.pet.dto.res.PetListResDto;
import com.strayanimal.petservice.pet.entity.StrayAnimalEntity;
import com.strayanimal.petservice.pet.repository.AnimalsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final AnimalsRepository animalsRepository;

    public CommonResDto search(SearchDto searchDto, int page) {

        Pageable pageable = PageRequest.of(
                page,      // 0부터 시작
                9          // 페이지당 아이템 수
        );

        Page<StrayAnimalEntity> result;
        if(searchDto.getRegion().equals("전라북도") || searchDto.getRegion().equals("전북특별자치도")) {
            result = animalsRepository.searchList2("전라북도", "전북특별자치도", searchDto.getKind(), pageable);
        }
        else if(searchDto.getRegion().equals("강원도") || searchDto.getRegion().equals("강원특별자치도")) {
            result = animalsRepository.searchList2("강원도", "강원특별자치도", searchDto.getKind(), pageable);
        }
        else if(searchDto.getRegion().equals("전체")) {
            result = animalsRepository.searchAll(searchDto.getKind(), pageable);
        }
        else {
            result = animalsRepository.searchList(searchDto.getRegion(), searchDto.getKind(), pageable);
        }

        Page<PetListResDto> response = result.map(PetListResDto::from);

        return new CommonResDto(HttpStatus.OK, "해당 지역의 유기동물 찾음", response);
    }

    public CommonResDto getDetail(String desertionNo) {

        return new CommonResDto(HttpStatus.FOUND, "해당 동물의 상세 정보 조회", getEntity(desertionNo));
    }


    private PetDetailResDto getEntity(String desertionNo) {
        Optional<StrayAnimalEntity> result = animalsRepository.findByDesertionNo(desertionNo);
        if(result.isPresent()) {
            return new PetDetailResDto(result.get());
        }
        else {
            throw new CommonException(ErrorCode.INVALID_PARAMETER, "없는 번호입니다.");
        }
    }

}
