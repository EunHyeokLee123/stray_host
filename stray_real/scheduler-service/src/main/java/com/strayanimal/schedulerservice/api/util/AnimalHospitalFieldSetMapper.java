package com.strayanimal.schedulerservice.api.util;

import com.strayanimal.schedulerservice.api.entity.AnimalHospital;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AnimalHospitalFieldSetMapper implements FieldSetMapper<AnimalHospital> {

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DateTimeFormatter dateTimeFormatterWithSeconds =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DateTimeFormatter dateTimeFormatterWithoutSeconds =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public AnimalHospital mapFieldSet(FieldSet fieldSet) throws BindException {

        AnimalHospital hospital = new AnimalHospital();

        // 0~2
        hospital.setId(fieldSet.readLong(0));
        hospital.setRegionCode(fieldSet.readString(1));
        hospital.setManagementNumber(fieldSet.readString(2));

        // 날짜
        hospital.setApprovalDate(parseLocalDate(fieldSet.readString(3)));
        hospital.setPermitCancelDate(parseLocalDate(fieldSet.readString(4)));
        hospital.setBusinessStatusName(fieldSet.readString(5));
        hospital.setClosureDate(parseLocalDate(fieldSet.readString(6)));

        hospital.setTemporaryClosureStartDate(parseLocalDate(fieldSet.readString(7)));
        hospital.setTemporaryClosureEndDate(parseLocalDate(fieldSet.readString(8)));
        hospital.setReopeningDate(parseLocalDate(fieldSet.readString(9)));

        // 숫자
        hospital.setSiteArea(parseDouble(fieldSet.readString(10)));

        // 문자열
        hospital.setPostalCode(fieldSet.readString(11));
        hospital.setRoadPostalCode(fieldSet.readString(12));
        hospital.setBusinessName(fieldSet.readString(13));
        hospital.setDataUpdateType(fieldSet.readString(14));
        hospital.setRightsHolderNumber(fieldSet.readString(15));

        // datetime
        hospital.setDataUpdateDate(parseLocalDateTime(fieldSet.readString(16)));

        // 주소
        hospital.setRoadAddress(fieldSet.readString(17));
        hospital.setDetailedBusinessStatusName(fieldSet.readString(18));
        hospital.setDetailedBusinessStatusCode(fieldSet.readString(19));
        hospital.setBusinessStatusCode(fieldSet.readString(20));

        hospital.setPhoneNumber(fieldSet.readString(21));

        // 좌표
        hospital.setCoordinateX(parseDouble(fieldSet.readString(22)));
        hospital.setCoordinateY(parseDouble(fieldSet.readString(23)));

        // 주소
        hospital.setFullAddress(fieldSet.readString(24));

        // 최종수정
        hospital.setLastModified(parseLocalDateTime(fieldSet.readString(25)));

        return hospital;
    }


    /* ================= 파싱 유틸 ================= */

    private LocalDate parseLocalDate(String value) {
        if (value == null || value.isBlank()) return null;

        try {
            return LocalDate.parse(value, dateFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.isBlank()) return null;

        try {
            return LocalDateTime.parse(value, dateTimeFormatterWithSeconds);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(value, dateTimeFormatterWithoutSeconds);
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
