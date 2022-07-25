package com.chayxana.chayxana.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Timer;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public Timer getTimer() {
        return new Timer();
    }



}