package com.strayanimal.mapservice.map.entity;

import com.strayanimal.mapservice.map.dto.hospital.res.HospitalDetailResDto;
import com.strayanimal.mapservice.map.dto.hospital.res.HospitalListDto;
import com.strayanimal.mapservice.map.util.Epsg5174ToWgs84Converter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.locationtech.proj4j.ProjCoordinate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "animal_hospital")
public class AnimalHospital {

    @Id
    private Long id;  // 번호

    @Column(name = "region_code")
    private String regionCode; // 개방자치단체코드

    @Column(name = "management_number")
    private String managementNumber; // 관리번호

    @Column(name = "approval_date")
    private LocalDate approvalDate; // 인허가일자

    @Column(name = "permit_cancel_date")
    private LocalDate permitCancelDate; // 인허가취소일자

    @Column(name = "business_status_name")
    private String businessStatusName; // 영업상태명

    @Column(name = "closure_date")
    private LocalDate closureDate; // 폐업일자

    @Column(name = "temporary_closure_start_date")
    private LocalDate temporaryClosureStartDate; // 휴업시작일자

    @Column(name = "temporary_closure_end_date")
    private LocalDate temporaryClosureEndDate; // 휴업종료일자

    @Column(name = "reopening_date")
    private LocalDate reopeningDate; // 재개업일자

    @Column(name = "site_area")
    private Double siteArea; // 소재지면적

    @Column(name = "postal_code")
    private String postalCode; // 소재지우편번호

    @Column(name = "road_postal_code")
    private String roadPostalCode; // 도로명우편번호

    @Column(name = "business_name")
    private String businessName; // 사업장명

    @Column(name = "data_update_type")
    private String dataUpdateType; // 데이터갱신구분

    @Column(name = "rights_holder_number")
    private String rightsHolderNumber; // 권리주체일련번호
    
    // 기존과 다르게 localDateTime됨
    @Column(name = "data_update_date")
    private LocalDateTime dataUpdateDate; // 데이터갱신일자

    @Column(name = "road_address")
    private String roadAddress; // 도로명전체주소

    @Column(name = "detailed_business_status_name")
    private String detailedBusinessStatusName; // 상세영업상태명

    @Column(name = "detailed_business_status_code")
    private String detailedBusinessStatusCode; // 상세영업상태코드

    @Column(name = "business_status_code")
    private String businessStatusCode;

    @Column(name = "phone_number")
    private String phoneNumber; // 소재지전화

    @Column(name = "coordinate_x")
    private Double coordinateX; // 좌표정보x(epsg5174)

    @Column(name = "coordinate_y")
    private Double coordinateY; // 좌표정보y(epsg5174)

    @Column(name = "full_address")
    private String fullAddress; // 소재지전체주소

    @Column(name = "last_modified")
    private LocalDateTime lastModified; // 최종수정시점

///    ////////////////

    public HospitalListDto listInfoFromEntity() {
        return HospitalListDto.builder()
                .hospitalId(this.id)
                .hospitalName(this.businessName)
                .fullAddress(fullAddress)
                .approvalDate(this.approvalDate)
                .build();
    }

    public HospitalDetailResDto detailInfoFromEntity() {

        // EPSG5174를 표준 위도, 경도로 변환
        double tempx = 0.0;
        double tempy = 0.0;
        if(this.coordinateX != null && this.coordinateY != null) {
            ProjCoordinate result
                    = Epsg5174ToWgs84Converter.convertEPSG5174ToWGS84(this.coordinateX, this.coordinateY);
            tempx = result.x;
            tempy = result.y;
        }

        return HospitalDetailResDto.builder()
                .id(this.id)
                .approvalDate(this.approvalDate)
                .phoneNumber(this.phoneNumber)
                .postalCode(this.postalCode)
                .fullAddress(this.fullAddress)
                .roadAddress(this.roadAddress)
                .roadPostalCode(this.roadPostalCode)
                .businessName(this.businessName)
                .lastModified(this.lastModified)
                .mapx(tempx)
                .mapy(tempy)
                .build();
    }

}