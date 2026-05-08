package com.strayanimal.schedulerservice.api.batch.dto;

import com.strayanimal.schedulerservice.api.entity.PetShelter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterDetail {

    private String desNo;

    private String careRegNo;
    private String dataStdDt;
    private String careNm;
    private String orgNm;
    private String divisionNm;
    private String saveTrgtAnimal;
    private String careAddr;
    private String jibunAddr;
    private String lat;
    private String lng;
    private String dsignationDate;
    private String weekOprStime;
    private String weekOprEtime;
    private String weekCellStime;
    private String weekCellEtime;
    private String weekendOprStime;
    private String weekendOprEtime;
    private String weekendCellStime;
    private String weekendCellEtime;
    private String closeDay;
    private String vetPersonCnt;
    private String specsPersonCnt;
    private String medicalCnt;
    private String breedCnt;
    private String quarabtineCnt;
    private String feedCnt;
    private String transCarCnt;
    private String careTel;


    public static ShelterDetail fromEntity(PetShelter input, String desNo) {
        return ShelterDetail.builder()
                .desNo(desNo)
                .careRegNo(input.getCareRegNo())
                .dataStdDt(input.getDataStdDt())
                .careNm(input.getCareNm())
                .orgNm(input.getOrgNm())
                .divisionNm(input.getDivisionNm())
                .saveTrgtAnimal(input.getSaveTrgtAnimal())
                .careAddr(input.getCareAddr())
                .jibunAddr(input.getJibunAddr())
                .lat(input.getLat())
                .lng(input.getLng())
                .dsignationDate(input.getDsignationDate())
                .weekOprStime(input.getWeekOprStime())
                .weekOprEtime(input.getWeekOprEtime())
                .weekCellStime(input.getWeekCellStime())
                .weekCellEtime(input.getWeekCellEtime())
                .weekendOprStime(input.getWeekendOprStime())
                .weekendOprEtime(input.getWeekendOprEtime())
                .weekendCellStime(input.getWeekendCellStime())
                .weekendCellEtime(input.getWeekendCellEtime())
                .closeDay(input.getCloseDay())
                .vetPersonCnt(input.getVetPersonCnt())
                .specsPersonCnt(input.getSpecsPersonCnt())
                .medicalCnt(input.getMedicalCnt())
                .breedCnt(input.getBreedCnt())
                .quarabtineCnt(input.getQuarabtineCnt())
                .feedCnt(input.getFeedCnt())
                .transCarCnt(input.getTransCarCnt())
                .careTel(input.getCareTel())
                .build();
    }
}
