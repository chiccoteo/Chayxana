package com.chayxana.chayxana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Chayxana {
    public static void main(String[] args) {
        SpringApplication.run(Chayxana.class, args);
    }
}
