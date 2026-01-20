package com.strayanimal.mapservice.map.dto.festival.res;

import com.strayanimal.mapservice.map.entity.FestivalEntity;
import com.strayanimal.mapservice.map.repository.FestivalRepository;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FestivalListResDto {

    private Long festivalId;
    private String title;
    private String url;
    private String location;
    private String festivalDate;
    private String addr;
    private String hash;


    public FestivalListResDto(FestivalEntity input) {
        this.festivalId = input.getFestivalId();
        this.title = input.getTitle();
        this.url = input.getUrl();
        this.location = input.getLocation();
        this.festivalDate = input.getFestivalDate();
        this.addr = input.getAddr();
        this.hash = input.getHash();
    }

}
