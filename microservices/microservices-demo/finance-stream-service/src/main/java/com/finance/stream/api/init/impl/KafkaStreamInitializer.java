package com.finance.stream.api.init.impl;

import com.finance.stream.api.init.StreamInitializer;
import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaStreamInitializer implements StreamInitializer {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;


    @Override
    public void init() {
        log.info("Initializing Kafka Stream");
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        log.info("Kafka Stream initialized");
    }
}
