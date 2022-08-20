package com.microservices.demo.analytics.service.converter;

import com.microservices.demo.analytics.service.entity.AnalyticsEntity;
import com.microservices.demo.analytics.service.model.AnalyticsResponseModel;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;


@Component
public class EntityToResponseModelTransformer {

    public Optional<AnalyticsResponseModel> getResponseModel(AnalyticsEntity analyticsEntity) {
        if (Objects.isNull(analyticsEntity))
            return Optional.empty();
        return Optional.ofNullable(AnalyticsResponseModel
                .builder()
                .id(analyticsEntity.getId())
                .shareName(analyticsEntity.getShareName())
                .shareVolume(analyticsEntity.getShareVolume())
                .build());
    }
}
