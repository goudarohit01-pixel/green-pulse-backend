package com.greenpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreenPulseApplication {
    public static void main(String[] args) {
         // Automatically use application-local.yml when running locally
        System.setProperty("spring.profiles.active", "local");
        SpringApplication.run(GreenPulseApplication.class, args);
    }
}