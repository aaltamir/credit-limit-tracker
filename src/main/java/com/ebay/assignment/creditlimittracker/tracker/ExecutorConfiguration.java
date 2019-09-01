package com.ebay.assignment.creditlimittracker.tracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfiguration {
    @Bean
    ExecutorService getExecutorService() {
        // 2 resources and 5 concurrent calls (supposed)
        return Executors.newFixedThreadPool(10);
    }
}
