package com.strayanimal.petservice.pet.dto.req;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class PetFavorReqDto {

    @Size(max = 6)
    private List<String> ids;

    @AssertTrue(message = "오직 숫자만 입력될 수 있습니다.")
    public boolean isAllNumeric() {
        // null이거나 빈 리스트인 경우 통과
        if(ids == null || ids.size() == 0) return true;

        for(String id : ids) {

            if(id == null || id.length() == 0) return false;

            for(int i = 0; i < id.length(); i++) {
                if(!Character.isDigit(id.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

}
