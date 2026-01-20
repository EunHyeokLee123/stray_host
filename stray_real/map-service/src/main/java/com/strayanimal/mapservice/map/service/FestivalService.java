package com.strayanimal.mapservice.map.service;

import com.strayanimal.mapservice.common.dto.CommonResDto;
import com.strayanimal.mapservice.common.dto.ErrorResponse;
import com.strayanimal.mapservice.common.enumeration.ErrorCode;
import com.strayanimal.mapservice.common.exception.CommonException;
import com.strayanimal.mapservice.map.dto.festival.res.FestivalListResDto;
import com.strayanimal.mapservice.map.entity.FestivalEntity;
import com.strayanimal.mapservice.map.repository.FestivalRepository;
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
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private static final int SIZE = 8;


    public CommonResDto getAllList(int page) {

        Pageable pageable = PageRequest.of(
                page,      // 0부터 시작
                SIZE          // 페이지당 아이템 수
        );

        Page<FestivalEntity> mid = festivalRepository.findValidList(pageable);
        Page<FestivalListResDto> result = mid.map(FestivalListResDto::new);

        return new CommonResDto(HttpStatus.OK, "Found valid festival", result);
    }

    public CommonResDto getDetail(Long id) {
        return new CommonResDto(HttpStatus.OK, "해당 축제 정보 찾음" ,getFestivalEntity(id));
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
