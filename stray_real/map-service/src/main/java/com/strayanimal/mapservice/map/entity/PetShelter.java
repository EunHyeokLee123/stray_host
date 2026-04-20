package com.strayanimal.mapservice.map.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pet_shelter")
@EntityListeners(AuditingEntityListener.class)
public class PetShelter {

    // 보호소 번호
    @Id
    @Column(name = "care_reg_no", nullable = false, unique = true)
    private String careRegNo;

    /** 데이터기준일자 */
    @Column(name = "data_std_dt")
    private String dataStdDt;

    /** 동물보호센터명 */
    @Column(name = "care_nm")
    private String careNm;

    /** 관리기관명 */
    @Column(name = "org_nm")
    private String orgNm;

    /** 동물보호센터유형 */
    @Column(name = "division_nm")
    private String divisionNm;

    /** 구조대상동물 */
    @Column(name = "save_trgt_animal")
    private String saveTrgtAnimal;

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

    /** 동물보호센터지정일자 */
    @Column(name = "dsignation_date")
    private String dsignationDate;

    /** 평일운영시작시각 */
    @Column(name = "week_opr_stime")
    private String weekOprStime;

    /** 평일운영종료시각 */
    @Column(name = "week_opr_etime")
    private String weekOprEtime;

    /** 평일분양시작시각 */
    @Column(name = "week_cell_stime")
    private String weekCellStime;

    /** 평일분양종료시각 */
    @Column(name = "week_cell_etime")
    private String weekCellEtime;

    /** 주말운영시작시각 */
    @Column(name = "weekend_opr_stime")
    private String weekendOprStime;

    /** 주말운영종료시각 */
    @Column(name = "weekend_opr_etime")
    private String weekendOprEtime;

    /** 주말분양시작시각 */
    @Column(name = "weekend_cell_stime")
    private String weekendCellStime;

    /** 주말분양종료시각 */
    @Column(name = "weekend_cell_etime")
    private String weekendCellEtime;

    /** 휴무일 */
    @Column(name = "close_day")
    private String closeDay;

    /** 수의사인원수 */
    @Column(name = "vet_person_cnt")
    private String vetPersonCnt;

    /** 사양관리사인원수 */
    @Column(name = "specs_person_cnt")
    private String specsPersonCnt;

    /** 진료실수 */
    @Column(name = "medical_cnt")
    private String medicalCnt;

    /** 사육실수 */
    @Column(name = "breed_cnt")
    private String breedCnt;

    /** 격리실수 */
    @Column(name = "quarabtine_cnt")
    private String quarabtineCnt;

    /** 사료보관실수 */
    @Column(name = "feed_cnt")
    private String feedCnt;

    /** 구조운반용차량보유대수 */
    @Column(name = "trans_car_cnt")
    private String transCarCnt;

    /** 전화번호 */
    @Column(name = "care_tel")
    private String careTel;

    public void updateIfChanged(PetShelter source) {

        if (source.getDataStdDt() != null && !source.getDataStdDt().equals(this.dataStdDt)) {
            this.dataStdDt = source.getDataStdDt();
        }
        if (source.getCareNm() != null && !source.getCareNm().equals(this.careNm)) {
            this.careNm = source.getCareNm();
        }
        if (source.getOrgNm() != null && !source.getOrgNm().equals(this.orgNm)) {
            this.orgNm = source.getOrgNm();
        }
        if (source.getDivisionNm() != null && !source.getDivisionNm().equals(this.divisionNm)) {
            this.divisionNm = source.getDivisionNm();
        }
        if (source.getSaveTrgtAnimal() != null && !source.getSaveTrgtAnimal().equals(this.saveTrgtAnimal)) {
            this.saveTrgtAnimal = source.getSaveTrgtAnimal();
        }
        if (source.getCareAddr() != null && !source.getCareAddr().equals(this.careAddr)) {
            this.careAddr = source.getCareAddr();
        }
        if (source.getJibunAddr() != null && !source.getJibunAddr().equals(this.jibunAddr)) {
            this.jibunAddr = source.getJibunAddr();
        }
        if (source.getLat() != null && !source.getLat().equals(this.lat)) {
            this.lat = source.getLat();
        }
        if (source.getLng() != null && !source.getLng().equals(this.lng)) {
            this.lng = source.getLng();
        }
        if (source.getDsignationDate() != null && !source.getDsignationDate().equals(this.dsignationDate)) {
            this.dsignationDate = source.getDsignationDate();
        }
        if (source.getWeekOprStime() != null && !source.getWeekOprStime().equals(this.weekOprStime)) {
            this.weekOprStime = source.getWeekOprStime();
        }
        if (source.getWeekOprEtime() != null && !source.getWeekOprEtime().equals(this.weekOprEtime)) {
            this.weekOprEtime = source.getWeekOprEtime();
        }
        if (source.getWeekCellStime() != null && !source.getWeekCellStime().equals(this.weekCellStime)) {
            this.weekCellStime = source.getWeekCellStime();
        }
        if (source.getWeekCellEtime() != null && !source.getWeekCellEtime().equals(this.weekCellEtime)) {
            this.weekCellEtime = source.getWeekCellEtime();
        }
        if (source.getWeekendOprStime() != null && !source.getWeekendOprStime().equals(this.weekendOprStime)) {
            this.weekendOprStime = source.getWeekendOprStime();
        }
        if (source.getWeekendOprEtime() != null && !source.getWeekendOprEtime().equals(this.weekendOprEtime)) {
            this.weekendOprEtime = source.getWeekendOprEtime();
        }
        if (source.getWeekendCellStime() != null && !source.getWeekendCellStime().equals(this.weekendCellStime)) {
            this.weekendCellStime = source.getWeekendCellStime();
        }
        if (source.getWeekendCellEtime() != null && !source.getWeekendCellEtime().equals(this.weekendCellEtime)) {
            this.weekendCellEtime = source.getWeekendCellEtime();
        }
        if (source.getCloseDay() != null && !source.getCloseDay().equals(this.closeDay)) {
            this.closeDay = source.getCloseDay();
        }
        if (source.getVetPersonCnt() != null && !source.getVetPersonCnt().equals(this.vetPersonCnt)) {
            this.vetPersonCnt = source.getVetPersonCnt();
        }
        if (source.getSpecsPersonCnt() != null && !source.getSpecsPersonCnt().equals(this.specsPersonCnt)) {
            this.specsPersonCnt = source.getSpecsPersonCnt();
        }
        if (source.getMedicalCnt() != null && !source.getMedicalCnt().equals(this.medicalCnt)) {
            this.medicalCnt = source.getMedicalCnt();
        }
        if (source.getBreedCnt() != null && !source.getBreedCnt().equals(this.breedCnt)) {
            this.breedCnt = source.getBreedCnt();
        }
        if (source.getQuarabtineCnt() != null && !source.getQuarabtineCnt().equals(this.quarabtineCnt)) {
            this.quarabtineCnt = source.getQuarabtineCnt();
        }
        if (source.getFeedCnt() != null && !source.getFeedCnt().equals(this.feedCnt)) {
            this.feedCnt = source.getFeedCnt();
        }
        if (source.getTransCarCnt() != null && !source.getTransCarCnt().equals(this.transCarCnt)) {
            this.transCarCnt = source.getTransCarCnt();
        }
        if (source.getCareTel() != null && !source.getCareTel().equals(this.careTel)) {
            this.careTel = source.getCareTel();
        }
    }

}
