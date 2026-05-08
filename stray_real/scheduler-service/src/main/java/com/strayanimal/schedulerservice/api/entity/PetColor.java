package com.strayanimal.schedulerservice.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

@Entity
@Table(name = "pet_color")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long colorId;

    @Column(unique = true, nullable = false)
    private String desertionNo;

    private boolean isPattern;

    private String color;

    private String upKindNm; // 축종 이름 (예: 개, 고양이)

    public static PetColor toEntity(String no, boolean isPattern, String color, String upKindNm) {
        return PetColor.builder()
                .desertionNo(no)
                .isPattern(isPattern)
                .color(color)
                .upKindNm(upKindNm)
                .build();
    }

    public void update(boolean isPattern, String color, String upKindNm) {
        if(this.isPattern != isPattern) {
            this.isPattern = isPattern;
        }
        if(this.color != color) {
            this.color = color;
        }
        if(this.upKindNm != upKindNm) {
            this.upKindNm = upKindNm;
        }
    }

}
