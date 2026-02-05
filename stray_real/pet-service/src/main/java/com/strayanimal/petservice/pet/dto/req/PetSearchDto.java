package com.strayanimal.petservice.pet.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
@Valid
public class PetSearchDto {

    @NotNull
    private String rfid;

}
