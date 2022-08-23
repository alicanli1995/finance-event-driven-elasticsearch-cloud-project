package com.microservices.demo.kafka.streams.service;

import com.microservices.demo.kafka.streams.service.init.StreamsInitializer;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.microservices.demo"})
public class KafkaStreamsServiceApplication implements CommandLineRunner {


	private final StreamsRunner<String, Long> streamsRunner;

	private final StreamsInitializer streamsInitializer;



	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("App starts...");
		streamsInitializer.init();
		streamsRunner.start();
	}

}