package com.gdsc_knu.official_homepage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OfficialHomepageApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfficialHomepageApplication.class, args);
    }

}
