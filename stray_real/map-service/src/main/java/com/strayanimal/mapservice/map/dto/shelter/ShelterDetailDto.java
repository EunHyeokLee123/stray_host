package com.strayanimal.mapservice.map.dto.shelter;

import com.strayanimal.mapservice.map.entity.PetShelter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShelterDetailDto {

    private String careRegNo;

    private String dataStdDt;

    private String careNm;

    private String orgNm;

    private String divisionNm;

    private String saveTrgtAnimal;

    private String careAddr;

    private String jibunAddr;

    /** 평일운영시작시각 */
    private String weekOprStime;

    /** 평일운영종료시각 */
    private String weekOprEtime;

    /** 평일분양시작시각 */
    private String weekCellStime;

    /** 평일분양종료시각 */
    private String weekCellEtime;

    /** 주말운영시작시각 */
    private String weekendOprStime;

    /** 주말운영종료시각 */
    private String weekendOprEtime;

    /** 주말분양시작시각 */
    private String weekendCellStime;

    /** 주말분양종료시각 */
    private String weekendCellEtime;

    /** 휴무일 */
    private String closeDay;

    /** 전화번호 */
    private String careTel;

    public ShelterDetailDto(PetShelter petShelter) {
        this.careRegNo = petShelter.getCareRegNo();
        this.dataStdDt = petShelter.getDataStdDt();
        this.careNm = petShelter.getCareNm();
        this.orgNm = petShelter.getOrgNm();
        this.divisionNm = petShelter.getDivisionNm();
        this.saveTrgtAnimal = petShelter.getSaveTrgtAnimal();
        this.careAddr = petShelter.getCareAddr();
        this.jibunAddr = petShelter.getJibunAddr();
        this.weekOprStime = petShelter.getWeekOprStime();
        this.weekOprEtime = petShelter.getWeekOprEtime();
        this.weekCellStime = petShelter.getWeekCellStime();
        this.weekCellEtime = petShelter.getWeekCellEtime();
        this.weekendOprStime = petShelter.getWeekendOprStime();
        this.weekendOprEtime = petShelter.getWeekendOprEtime();
        this.weekendCellStime = petShelter.getWeekendCellStime();
        this.weekendCellEtime = petShelter.getWeekendCellEtime();
        this.closeDay = petShelter.getCloseDay();
        this.careTel = petShelter.getCareTel();
    }

}
