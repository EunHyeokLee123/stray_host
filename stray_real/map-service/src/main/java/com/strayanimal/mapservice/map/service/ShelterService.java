package com.strayanimal.mapservice.map.service;

import com.strayanimal.mapservice.common.dto.CommonResDto;
import com.strayanimal.mapservice.common.enumeration.ErrorCode;
import com.strayanimal.mapservice.common.exception.CommonException;
import com.strayanimal.mapservice.map.dto.shelter.ShelterDetailDto;
import com.strayanimal.mapservice.map.dto.shelter.ShelterListDto;
import com.strayanimal.mapservice.map.entity.CultureDetail.CultureAddressCode;
import com.strayanimal.mapservice.map.entity.PetShelter;
import com.strayanimal.mapservice.map.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;


    public CommonResDto findByRegion(String region) {

        String targetRegion = CultureAddressCode.from(region).getDesc();
        List<PetShelter> res;
        
        // 전북
        if(region.equals("37")) {
            res = shelterRepository.getShelterList2(List.of("전라북도", "전북특별자치도"));
        }
        // 강원
        else if(region.equals("32")){
            res = shelterRepository.getShelterList2(List.of("강원도", "강원특별자치도"));
        }
        // 세종
        else if(region.equals("8")) {
            res = shelterRepository.getShelterList2(List.of("세종시", "세종특별자치시"));
        }
        // 제주
        else if(region.equals("39")) {
            res = shelterRepository.getShelterList2(List.of("제주도", "제주특별자치도"));
        }
        else {
            res = shelterRepository.getShelterList(targetRegion);
        }

        return new CommonResDto(HttpStatus.FOUND, "잘 찾음", res.stream().map(ShelterListDto::new).toList());

    }


    public CommonResDto findDetail(String id) {

        Optional<PetShelter> found = shelterRepository.findById(id);

        if(found.isPresent()) {
            return new CommonResDto(HttpStatus.FOUND, "찾았습니다.", new ShelterDetailDto(found.get()));
        }
        else {
            throw new CommonException(ErrorCode.BAD_REQUEST, "없는 보호소 id입니다.");
        }
    }

}
