package com.microservices.demo.kafka.to.elastic.service.converter;

import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
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
                .map(avroModel -> FinanceIndexModel
                        .builder()
                        .id(avroModel.getId())
                        .shareData(FinanceAvroDTO
                                .builder()
                                .c(avroModel.getShareData().getC())
                                .dailyChangePercentage(avroModel.getShareData().getDailyChangePercentage())
                                .dailyVolume(avroModel.getShareData().getDailyVolume())
                                .description(avroModel.getShareData().getDescription())
                                .dailyChange(avroModel.getShareData().getDailyChange())
                                .previousDayClose(avroModel.getShareData().getPreviousDayClose())
                                .last(avroModel.getShareData().getLast())
                                .build())
                        .createdAt(ZonedDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt()),
                                ZoneId.systemDefault()))
                        .build()
                ).toList();
    }

}
