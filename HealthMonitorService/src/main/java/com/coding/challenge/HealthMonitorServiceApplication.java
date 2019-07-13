package com.coding.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthMonitorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthMonitorServiceApplication.class, args);
    }

}
