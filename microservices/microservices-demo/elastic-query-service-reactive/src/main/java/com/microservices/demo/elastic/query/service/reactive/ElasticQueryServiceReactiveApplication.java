package com.microservices.demo.elastic.query.service.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.microservices.demo"})
public class ElasticQueryServiceReactiveApplication  {
    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryServiceReactiveApplication.class, args);
    }

}
