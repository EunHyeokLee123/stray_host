package com.strayanimal.mapservice.map.controller;

import com.strayanimal.mapservice.common.dto.CommonResDto;
import com.strayanimal.mapservice.map.service.FestivalService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/festival")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/list/{page}")
    public ResponseEntity<?> getFestivalList(@PathVariable @Min(value = 0, message = "page는 0 이상이어야 합니다.") int page) {
        CommonResDto resDto = festivalService.getAllList(page);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getFestivalDetail(@PathVariable Long id) {
        CommonResDto resDto = festivalService.getDetail(id);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
