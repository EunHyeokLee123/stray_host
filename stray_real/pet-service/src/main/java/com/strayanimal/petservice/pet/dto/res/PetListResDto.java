package com.strayanimal.petservice.pet.dto.res;

import com.strayanimal.petservice.pet.entity.StrayAnimalEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetListResDto {

    private String desertionNo; // 유기동물 고유번호, PK 역할
    private String upKindNm; // 축종 이름 (예: 개, 고양이)
    private String kindNm; // 품종 이름
    private String age; // 나이 정보
    private String popfile1; // 대표 이미지 URL
    private StrayAnimalEntity.SexCode sexCd; // 성별 코드 (M: 수컷, F: 암컷, Q: 미상)
    private String careTel; // 보호소 전화번호
    private String careAddr; // 보호소 주소
    private String happenDt; // 유기발생 날짜
    private StrayAnimalEntity.NeuterYn neuterYn; // 중성화 여부 (Y: 예, N: 아니오, U: 미상)

    public static PetListResDto from(StrayAnimalEntity input) {
        return PetListResDto.builder()
                .desertionNo(input.getDesertionNo())
                .upKindNm(input.getUpKindNm())
                .kindNm(input.getKindNm())
                .age(input.getAge())
                .popfile1(input.getPopfile1())
                .sexCd(input.getSexCd())
                .careTel(input.getCareTel())
                .careAddr(input.getCareAddr())
                .happenDt(input.getHappenDt())
                .neuterYn(input.getNeuterYn())
                .build();
    }


}
