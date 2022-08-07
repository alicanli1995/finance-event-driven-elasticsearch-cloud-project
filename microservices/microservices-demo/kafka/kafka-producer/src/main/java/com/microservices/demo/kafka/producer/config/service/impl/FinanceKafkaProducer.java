package com.microservices.demo.kafka.producer.config.service.impl;

import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import com.microservices.demo.kafka.producer.config.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceKafkaProducer implements KafkaProducer<String, FinanceAvroModel> {

    private final KafkaTemplate<String, FinanceAvroModel> kafkaTemplate;


    @Override
    public void send(String topic, String key, FinanceAvroModel message) {
        log.info("Sending message='{}' to topic='{}' with key='{}'", message, topic, key);
        var result =
                kafkaTemplate.send(topic, key, message);
        addCallback(topic, key, message, result);


    }

    @PreDestroy
    public void destroy() {
        if (Objects.nonNull(kafkaTemplate)) {
            log.info("Closing kafkaTemplate");
            kafkaTemplate.destroy();
        }
    }

    private static void addCallback(String topic, String key, FinanceAvroModel message, ListenableFuture<SendResult<String, FinanceAvroModel>> result) {
        result.addCallback(new ListenableFutureCallback<SendResult<String, FinanceAvroModel>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error sending message='{}' to topic='{}' with key='{}'", message, topic, key, ex);
            }

            @Override
            public void onSuccess(SendResult<String, FinanceAvroModel> result) {
                var metaData = result.getRecordMetadata();
                log.debug("Message sent to topic= {} ,  Partition= {} , offset= {}, Timestamp= {} , at time= {}",
                        metaData.topic(),
                        metaData.partition(),
                        metaData.offset(),
                        metaData.timestamp(),
                        System.nanoTime());
            }
        });
    }
}
