package com.strayanimal.schedulerservice.crawling.helper;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcs")
@Getter
@Setter
public class gcsSetting {

    String bucket;
    String baseUrl;

    @PostConstruct
    public void check() {
        System.out.println("==== GCS SETTING CHECK ====");
        System.out.println("bucket = " + bucket);
        System.out.println("baseUrl = " + baseUrl);
        System.out.println("===========================");
    }

}
