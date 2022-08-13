package com.microservices.demo.elastic.query.service.common.converter;

import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElasticToResponseModelConverter {

    public ElasticQueryResponseModel convert(FinanceIndexModel financeIndexModel) {
        return ElasticQueryResponseModel.builder()
                .id(financeIndexModel.getId())
                .shareData(
                        FinanceAvroDTO.builder()
                                .dailyChangePercentage(financeIndexModel.getShareData().getDailyChangePercentage())
                                .dailyChange(financeIndexModel.getShareData().getDailyChange())
                                .c(financeIndexModel.getShareData().getC())
                                .last(financeIndexModel.getShareData().getLast())
                                .dailyVolume(financeIndexModel.getShareData().getDailyVolume())
                                .previousDayClose(financeIndexModel.getShareData().getPreviousDayClose())
                                .description(financeIndexModel.getShareData().getDescription())
                                .build()
                )
                .createdAt(financeIndexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQueryResponseModel> convert(List<FinanceIndexModel> financeIndexModels) {
        return financeIndexModels.stream()
                .map(this::convert)
                .toList();
    }

}
