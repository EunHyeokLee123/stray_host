package com.strayanimal.schedulerservice.crawling.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "festival")
public class FestivalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalId;
    private String source;
    private String title;
    private String url;
    private String location;
    private String festivalDate;
    private String festivalTime;
    private String money;
    private String imagePath;
    private String reservationDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String addr;
    @Column(nullable = false, unique = true)
    private String hash;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateFrom(FestivalEntity sourceEvent) {
        this.source = sourceEvent.getSource();
        this.title = sourceEvent.getTitle();
        this.url = sourceEvent.getUrl();
        this.location = sourceEvent.getLocation();
        this.festivalDate = sourceEvent.getFestivalDate();
        this.festivalTime = sourceEvent.getFestivalTime();
        this.money = sourceEvent.getMoney();
        this.imagePath = sourceEvent.getImagePath();
        this.reservationDate = sourceEvent.getReservationDate();
        this.startDate = sourceEvent.getStartDate();
        this.endDate = sourceEvent.getEndDate();
    }
}
