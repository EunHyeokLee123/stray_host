package com.strayanimal.mapservice.map.dto.shelter;

import com.strayanimal.mapservice.map.entity.PetShelter;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShelterListDto {

    // 보호소 번호
    private String careRegNo;

    /** 동물보호센터명 */
    @Column(name = "care_nm")
    private String careNm;

    /** 소재지도로명주소 */
    @Column(name = "care_addr")
    private String careAddr;

    /** 소재지번주소 */
    @Column(name = "jibun_addr")
    private String jibunAddr;

    /** 위도 */
    @Column(name = "lat")
    private String lat;

    /** 경도 */
    @Column(name = "lng")
    private String lng;

    public ShelterListDto(PetShelter source) {
        this.careRegNo = source.getCareRegNo();
        this.careNm = source.getCareNm();
        this.careAddr = source.getCareAddr();
        this.jibunAddr = source.getJibunAddr();
        this.lat = source.getLat();
        this.lng = source.getLng();
    }

}
