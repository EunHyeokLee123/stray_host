package com.strayanimal.mapservice.map.controller;

import com.strayanimal.mapservice.common.dto.CommonResDto;
import com.strayanimal.mapservice.map.service.ShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shelter")
@RequiredArgsConstructor
@Slf4j
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping("/list/{region}")
    public ResponseEntity<?> getList(@PathVariable String region) {

        CommonResDto resDto = shelterService.findByRegion(region);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) {

        CommonResDto resDto = shelterService.findDetail(id);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
