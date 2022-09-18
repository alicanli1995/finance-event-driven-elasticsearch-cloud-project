package com.microservices.demo.analytics.service.converter;


import com.microservices.demo.analytics.service.entity.AnalyticsEntity;
import com.microservices.demo.kafka.avro.model.FinanceAnalyticsAvroModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AvroToDbEntityModelTransformer {

    private final IdGenerator idGenerator;


    public List<AnalyticsEntity> getEntityModel(List<FinanceAnalyticsAvroModel> avroModels) {
        return avroModels.stream()
                .map(avroModel -> {
                    var shareInformation = avroModel.getShareVolume().split(" ");
                    return new AnalyticsEntity(
                            idGenerator.generateId()
                            , shareInformation[0].replaceAll(":", "")
                            , shareInformation[1]);
                })
                .peek(x -> x.setCreatedBy("SYSTEM"))
                .toList();
    }


}
