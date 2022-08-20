package com.microservices.demo.analytics.service.service.impl;

import com.microservices.demo.analytics.service.converter.AvroToDbEntityModelTransformer;
import com.microservices.demo.analytics.service.repository.AnalyticsRepository;
import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.FinanceAnalyticsAvroModel;
import com.microservices.demo.kafka.consumer.config.api.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsKafkaConsumer implements KafkaConsumer<FinanceAnalyticsAvroModel> {


    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaAdminClient kafkaAdminClient;

    private final AvroToDbEntityModelTransformer avroToDbEntityModelTransformer;

    private final AnalyticsRepository analyticsRepository;

    private final KafkaConfigData kafkaConfig;

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with name {} is ready for operations!", kafkaConfig.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.
                getListenerContainer("financeAnalyticsTopicListener")).start();
    }

    @Override
    @KafkaListener(id = "financeAnalyticsTopicListener", topics = "${kafka-config.topic-name}", autoStartup = "false")
    public void receive(@Payload List<FinanceAnalyticsAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Long> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        var entities = avroToDbEntityModelTransformer.getEntityModel(messages);
        analyticsRepository.batchPersist(entities);
        log.info("{} number of messages received from topic {}", messages.size(), kafkaConfig.getTopicName());
    }

}
