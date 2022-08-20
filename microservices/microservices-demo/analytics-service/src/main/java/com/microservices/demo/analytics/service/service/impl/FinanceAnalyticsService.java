package com.microservices.demo.analytics.service.service.impl;

import com.microservices.demo.analytics.service.converter.EntityToResponseModelTransformer;
import com.microservices.demo.analytics.service.model.AnalyticsResponseModel;
import com.microservices.demo.analytics.service.repository.AnalyticsRepository;
import com.microservices.demo.analytics.service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceAnalyticsService implements AnalyticsService {
    private final AnalyticsRepository analyticsRepository;
    private final EntityToResponseModelTransformer entityToResponseModelTransformer;
    @Override
    public Optional<AnalyticsResponseModel> getShareNameAnalytics(String shareName) {
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.getAnalyticsEntitiesByShareName(shareName, PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No data found for shareName: " + shareName)));
    }
}
