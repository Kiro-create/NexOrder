package com.eoms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eoms")
public class NexOrderApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(NexOrderApiApplication.class, args);
    }
}