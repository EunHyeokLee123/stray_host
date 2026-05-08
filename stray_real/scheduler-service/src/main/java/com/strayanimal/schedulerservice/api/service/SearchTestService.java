package com.strayanimal.schedulerservice.api.service;

import com.strayanimal.schedulerservice.api.dto.CategoryDto;
import com.strayanimal.schedulerservice.api.dto.PetListResDto;
import com.strayanimal.schedulerservice.api.repository.AnimalsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchTestService {

    private final AnimalsRepository animalsRepository;

    public Page<PetListResDto> getPetSearch(CategoryDto dto, int page) {
        Page<PetListResDto> found = animalsRepository.getRightAnimals(dto, page);

        return found;
    }

}
