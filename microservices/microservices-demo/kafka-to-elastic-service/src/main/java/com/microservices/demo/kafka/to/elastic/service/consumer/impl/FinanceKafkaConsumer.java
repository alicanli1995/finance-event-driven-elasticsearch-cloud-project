package com.microservices.demo.kafka.to.elastic.service.consumer.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaConsumerConfigData;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import com.microservices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.microservices.demo.kafka.to.elastic.service.converter.AvroToElasticConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceKafkaConsumer implements KafkaConsumer<String , FinanceAvroModel> {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    private final KafkaConsumerConfigData kafkaConsumerConfigData;
    private final AvroToElasticConverter avroToElasticConverter;

    private final ElasticIndexClient<FinanceIndexModel> elasticIndexClient;


    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id" , topics = "${kafka-config.topic-name}")
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

        var financeIndexModels = avroToElasticConverter.getElasticModels(messages);
        var savingIndex = elasticIndexClient.save(financeIndexModels);
        log.info("Documents saved to elasticsearch with ids {}",savingIndex.toString());
    }

//    @EventListener
//    public void onAppStarted(ApplicationStartedEvent event) {
//        kafkaAdminClient.checkTopicsCreated();
//        log.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
//        Objects.requireNonNull(kafkaListenerEndpointRegistry
//                .getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId())).start();
//    }

}

