package com.strayanimal.mapservice.map.service;

import com.strayanimal.mapservice.common.dto.CommonResDto;
import com.strayanimal.mapservice.common.dto.ErrorResponse;
import com.strayanimal.mapservice.common.enumeration.ErrorCode;
import com.strayanimal.mapservice.common.exception.CommonException;
import com.strayanimal.mapservice.map.dto.festival.res.FestivalListResDto;
import com.strayanimal.mapservice.map.entity.FestivalEntity;
import com.strayanimal.mapservice.map.repository.FestivalRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private static final int SIZE = 8;


    public CommonResDto getAllList(int page) {

        Pageable pageable = PageRequest.of(
                page,      // 0부터 시작
                SIZE,          // 페이지당 아이템 수
                Sort.by("startDate").ascending()
        );

        Page<FestivalEntity> mid = festivalRepository.findValidList(pageable, LocalDate.now());
        Page<FestivalListResDto> result = mid.map(FestivalListResDto::new);

        return new CommonResDto(HttpStatus.OK, "Found valid festival", result);
    }

    public CommonResDto getDetail(Long id) {
        return new CommonResDto(HttpStatus.OK, "해당 축제 정보 찾음" ,getFestivalEntity(id));
    }

    public CommonResDto getRegion() {

        List<String> region = festivalRepository.findRegion(LocalDate.now());

        return new CommonResDto(HttpStatus.OK, "지역명 모두 찾음", region);
    }

    public CommonResDto findByRegion(String region, int page) {

        Pageable pageable = PageRequest.of(
                page,      // 0부터 시작
                SIZE,          // 페이지당 아이템 수
                Sort.by("startDate").ascending()
        );

        Page<FestivalEntity> mid = festivalRepository.findByRegion(pageable, region, LocalDate.now());
        Page<FestivalListResDto> result = mid.map(FestivalListResDto::new);

        return new CommonResDto(HttpStatus.OK, "Found valid festival", result);
    }

    private FestivalEntity getFestivalEntity(Long id) {
        Optional<FestivalEntity> found = festivalRepository.findId(id);
        if(found.isPresent()) {
            return found.get();
        }
        else {
            throw new CommonException(ErrorCode.INVALID_PARAMETER, "Wrong ID");
        }
    }
}
