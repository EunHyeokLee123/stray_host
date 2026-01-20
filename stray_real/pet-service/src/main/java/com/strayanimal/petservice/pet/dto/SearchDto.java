package com.strayanimal.petservice.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    // 지역 대분류 (예: 경기도, 서울특별시)
    @NotBlank(message = "region은 필수 값입니다.")
    private String region;

    // 축종 개, 고양이, 기타
    @NotBlank(message = "kind는 필수 값입니다.")
    @Pattern(
            regexp = "개|고양이|기타",
            message = "kind는 개, 고양이, 기타 중 하나여야 합니다."
    )
    private String kind;

}
