package com.strayanimal.schedulerservice.crawling.controller;

import com.strayanimal.schedulerservice.crawling.crawler.NaverPetEventCrawler;
import com.strayanimal.schedulerservice.crawling.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PetController {

    private final NaverPetEventCrawler crawler;
    private final PetRepository petRepository;

    @GetMapping("/scheduler/crawler")
    public String runCrawler() {
        try {
            crawler.crawl();
            return "크롤링이 완료되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "크롤링 도중 오류가 발생했습니다.";
        }
    }


}