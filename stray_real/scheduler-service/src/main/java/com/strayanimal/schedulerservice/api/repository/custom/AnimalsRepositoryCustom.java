package com.strayanimal.schedulerservice.api.repository.custom;

import com.strayanimal.schedulerservice.api.dto.CategoryDto;
import com.strayanimal.schedulerservice.api.dto.PetListResDto;
import org.springframework.data.domain.Page;

public interface AnimalsRepositoryCustom {

    Page<PetListResDto> getRightAnimals(CategoryDto dto, int page);

}
