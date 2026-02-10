package com.strayanimal.petservice.pet.service;

import com.strayanimal.petservice.common.dto.CommonResDto;
import com.strayanimal.petservice.common.enumeration.ErrorCode;
import com.strayanimal.petservice.common.exception.CommonException;
import com.strayanimal.petservice.pet.dto.SearchDto;
import com.strayanimal.petservice.pet.dto.req.PetFavorReqDto;
import com.strayanimal.petservice.pet.dto.req.PetSearchDto;
import com.strayanimal.petservice.pet.dto.res.PetDetailResDto;
import com.strayanimal.petservice.pet.dto.res.PetListResDto;
import com.strayanimal.petservice.pet.entity.StrayAnimalEntity;
import com.strayanimal.petservice.pet.repository.AnimalsRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final AnimalsRepository animalsRepository;

    public CommonResDto search(SearchDto searchDto, int page) {

        int count = getItemCount(searchDto.getDevice());

        Pageable pageable = PageRequest.of(
                page,      // 0부터 시작
                count          // 페이지당 아이템 수
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


    public CommonResDto getTotalCount() {

        Integer result = animalsRepository.countAllAnimal();

        return new CommonResDto(HttpStatus.OK, "모든 유기동물의 수 찾음", result);
    }

    public CommonResDto findByRfid(@Valid PetSearchDto searchDto) {

        Optional<StrayAnimalEntity> found = animalsRepository.findByRfid(isValidRfid(searchDto.getRfid()));
        if(found.isPresent()) {
            return new CommonResDto(HttpStatus.FOUND, "해당하는 아이 찾음", new PetDetailResDto(found.get()));
        }
        else {
            return new CommonResDto(HttpStatus.OK, "해당하는 아이 없음", null);
        }
    }

    public CommonResDto findCountRfid() {

        Integer result = animalsRepository.countRfid();

        return new CommonResDto(HttpStatus.OK, "등록칩이 있는 동물의 수 찾음", result);
    }

    public CommonResDto findMyFavor(@Valid PetFavorReqDto reqDto) {

        List<StrayAnimalEntity> mid = animalsRepository.findMyFavor(reqDto.getIds());

        List<PetListResDto> resDto = mid.stream().map(PetListResDto::from).toList();

        return new CommonResDto(HttpStatus.OK, "즐겨찾기 목록 찾음", resDto);
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

    private int getItemCount(int device) {
        if(device == 0) {
            return 9;
        }
        else if(device == 1) {
            return 8;
        }
        else if(device == 2) {
            return 6;
        }
        else {
            throw new CommonException(ErrorCode.INVALID_PARAMETER, "옳지 않은 기기정보입니다.");
        }
    }

    private String isValidRfid(String input) {
        for (int i = 0; i < input.length(); i++) {
            if(!Character.isDigit(input.charAt(i))) {
                throw new CommonException(ErrorCode.BAD_REQUEST, "옳지 않은 값입니다.");
            }
        }
        return input;
    }
}
