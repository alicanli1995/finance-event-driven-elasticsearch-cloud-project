package com.finance.stream.api;

import com.finance.stream.api.init.StreamInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.microservices.demo",
                               "com.finance.stream.api"})
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class FinanceStreamApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(FinanceStreamApp.class, args);
    }

    private final StreamInitializer streamInitializer;
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing Kafka Stream");
        streamInitializer.init();
    }
}
