package com.strayanimal.schedulerservice.api.service;

import com.strayanimal.schedulerservice.api.entity.PetColor;
import com.strayanimal.schedulerservice.api.repository.AnimalsRepository;
import com.strayanimal.schedulerservice.api.repository.PetColorRepository;
import com.strayanimal.schedulerservice.api.util.AnimalColorExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ColorService {

    private final AnimalsRepository animalsRepository;
    private final PetColorRepository petColorRepository;

    // 모든 유기동물의 색상정보 정규화 테이블 최신화
    public Map<String, List<String>> getColorMap() {
        Map<String, List<String>> result = new HashMap<>();

        animalsRepository.findAll().forEach(animal -> {
            List<String> colors = AnimalColorExtractor.extractColors(animal.getColorCd());
            if(!colors.isEmpty()) {
                Optional<PetColor> found = petColorRepository.findByDesertionNo(animal.getDesertionNo());
                if(colors.get(0).equals("pattern")) {
                    colors.remove(0);
                    if(found.isPresent()) {
                        found.get().update(true, makeOneLine(colors), animal.getUpKindNm());
                    }
                    else{
                        petColorRepository.save(PetColor.toEntity(animal.getDesertionNo(), true, makeOneLine(colors), animal.getUpKindNm()));
                    }
                }
                else {
                    if(found.isPresent()) {
                        found.get().update(false, makeOneLine(colors), animal.getUpKindNm());
                    }
                    else{
                        petColorRepository.save(PetColor.toEntity(animal.getDesertionNo(), false, makeOneLine(colors), animal.getUpKindNm()));
                    }
                }
            }

            result.put(animal.getDesertionNo(), colors);
        });

        return result;
    }

    private String makeOneLine(List<String> input) {
        StringBuilder result = new StringBuilder();
        input = input.stream().sorted().toList();

        for(int i = 0; i < input.size() - 1; i++) {
            result.append(input.get(i)).append(", ");
        }
        result.append(input.get(input.size() - 1));

        return result.toString();
    }


}
