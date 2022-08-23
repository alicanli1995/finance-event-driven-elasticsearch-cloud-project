package com.microservices.demo.discovery.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DiscoveryClientApp {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryClientApp.class, args);
    }
}
