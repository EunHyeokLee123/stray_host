package com.strayanimal.petservice.pet.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetListReqDto {

    // 지역 대분류
    String region;
    // 성별 M, F, Q
    String sex;
    // 축종 D, C, E (dog, cat, etc)
    String kind;


}
