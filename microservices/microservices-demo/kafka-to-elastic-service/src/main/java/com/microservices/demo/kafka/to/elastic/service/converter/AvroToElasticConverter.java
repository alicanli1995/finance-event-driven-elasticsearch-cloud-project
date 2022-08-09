package com.microservices.demo.kafka.to.elastic.service.converter;

import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class AvroToElasticConverter {

    public List<FinanceIndexModel> getElasticModels(List<FinanceAvroModel> avroModels){

        return avroModels.stream()
                .map(avroModel -> {
                            String text = "Share Name :".concat(avroModel.getShareData().getC()) +
                                    " Share Last Volume : " + avroModel.getShareData().getLast();
                            return FinanceIndexModel
                                    .builder()
                                    .id(avroModel.getId())
                                    .shareData(text)
                                    .createdAt(ZonedDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt()),
                                            ZoneId.systemDefault()))
                                    .build();
                        }
                ).toList();
    }

}
