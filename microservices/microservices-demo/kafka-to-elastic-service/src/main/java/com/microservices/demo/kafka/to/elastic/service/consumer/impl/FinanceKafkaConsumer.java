package com.microservices.demo.kafka.to.elastic.service.consumer.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import com.microservices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceKafkaConsumer implements KafkaConsumer<String , FinanceAvroModel> {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;


    @Override
    @KafkaListener(id = "financeTopicListener" , topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<FinanceAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of message received with keys {}, partitions {} and offsets {}, " +
                        "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent applicationStartedEvent) {

        kafkaAdminClient.checkTopicsCreated();
        log.info("Topic with name {} is ready for processing...", kafkaConfigData.getTopicName());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer("financeTopicListener")).start();

    }
}

