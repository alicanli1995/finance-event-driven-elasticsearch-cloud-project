package com.microservices.demo.analytics.service.converter;

import com.microservices.demo.analytics.service.entity.AnalyticsEntity;
import com.microservices.demo.analytics.service.model.AnalyticsResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class EntityToResponseModelTransformer {

    public List<AnalyticsResponseModel> getResponseModel(List<AnalyticsEntity> analyticsEntity) {
        return Optional.ofNullable(analyticsEntity)
                .map(analyticsEntities -> analyticsEntities.stream()
                        .map(this::getResponseModelSingle)
                        .toList())
                .orElse(List.of());
    }

    public AnalyticsResponseModel getResponseModelSingle(AnalyticsEntity analyticsEntity) {
        return AnalyticsResponseModel.builder()
                .id(analyticsEntity.getId())
                .shareName(analyticsEntity.getShareName())
                .shareVolume(analyticsEntity.getShareVolume())
                .recordTime(analyticsEntity.getCreatedDate())
                .build();
    }
}
