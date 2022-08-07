package com.finance.stream.api.service.impl;

import com.finance.stream.api.converter.FinanceToAvroConverter;
import com.finance.stream.api.dto.FinanceApiDTO;
import com.finance.stream.api.service.ProcessData;
import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import com.microservices.demo.kafka.producer.config.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaStreamProcessor implements ProcessData {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<String, FinanceAvroModel> kafkaProducer;
    private final FinanceToAvroConverter financeToAvroConverter;

    @Override
    public void processData(List<FinanceApiDTO> financeApiDTOs) {

        log.info("Received data from finance api, and sending kafka topic: {} , data is : {}",
                kafkaConfigData.getTopicName(),
                financeApiDTOs.get(0));

        var financeAvroModel =
                financeToAvroConverter.getFinanceAvroModel(financeApiDTOs.get(0));

        kafkaProducer.send(kafkaConfigData.getTopicName(), financeAvroModel.getId(), financeAvroModel);


    }
}
