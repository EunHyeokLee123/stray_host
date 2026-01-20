package com.strayanimal.petservice.pet.controller;

import com.strayanimal.petservice.common.dto.CommonResDto;
import com.strayanimal.petservice.pet.dto.SearchDto;
import com.strayanimal.petservice.pet.service.PetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
@Validated
public class PetController {

    private final PetService petService;
    
    // 지역과 축종을 받아서 페이징 목록 조회
    @PostMapping("/search/{page}")
    public ResponseEntity<?> search(@RequestBody @Valid SearchDto searchDto,
                                    @PathVariable @Min(value = 0, message = "page는 0 이상이어야 합니다.") int page) {
        CommonResDto resDto = petService.search(searchDto, page);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // pk로 특정 동물의 상세정보 조회
    @GetMapping("/detail/{desertionNo}")
    public ResponseEntity<?> getDetail(@PathVariable String desertionNo) {

        CommonResDto resDto = petService.getDetail(desertionNo);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    


}
