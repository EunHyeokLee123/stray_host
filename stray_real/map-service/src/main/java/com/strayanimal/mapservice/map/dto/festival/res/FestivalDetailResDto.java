package com.strayanimal.mapservice.map.dto.festival.res;

import com.strayanimal.mapservice.map.entity.FestivalEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FestivalDetailResDto {

    private Long festivalId;
    private String title;
    private String url;
    private String location;
    private String festivalDate;
    private String festivalTime;
    private String money;
    private String imagePath;
    private String reservationDate;
    private String addr;
    private String hash;

    public FestivalDetailResDto(FestivalEntity input) {
        this.festivalId = input.getFestivalId();
        this.title = input.getTitle();
        this.url = input.getUrl();
        this.location = input.getLocation();
        this.festivalDate = input.getFestivalDate();
        this.festivalTime = input.getFestivalTime();
        this.money = input.getMoney();
        this.imagePath = input.getImagePath();
        this.reservationDate = input.getReservationDate();
        this.addr = input.getAddr();
        this.hash = input.getHash();
    }

}
