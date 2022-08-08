package com.microservices.demo.kafka.to.elastic.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class KafkaToElasticServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(KafkaToElasticServiceApp.class, args);
    }
}
