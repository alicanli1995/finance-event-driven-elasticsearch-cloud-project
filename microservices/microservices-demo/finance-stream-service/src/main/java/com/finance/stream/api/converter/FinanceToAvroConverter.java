package com.finance.stream.api.converter;

import com.finance.stream.api.dto.FinanceApiDTO;
import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import com.microservices.demo.kafka.avro.model.Share;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class FinanceToAvroConverter {

    public FinanceAvroModel getFinanceAvroModel(FinanceApiDTO finance) {
        return FinanceAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setCreatedAt(new Date().getTime())
                .setShareData(Share.newBuilder()
                        .setC(finance.c().substring(0, finance.c().indexOf(".")))
                        .setDailyChange(finance.dailyChange())
                        .setDescription(finance.description())
                        .setLast(finance.last())
                        .setDailyChangePercentage(finance.dailyChangePercentage())
                        .setDailyVolume(finance.dailyVolume())
                        .setPreviousDayClose(finance.previousDayClose())
                        .build())
                .build();
    }


}
