package com.microservices.demo.analytics.service.service.impl;

import com.microservices.demo.analytics.service.converter.EntityToResponseModelTransformer;
import com.microservices.demo.analytics.service.model.AnalyticsResponseModel;
import com.microservices.demo.analytics.service.repository.AnalyticsRepository;
import com.microservices.demo.analytics.service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceAnalyticsService implements AnalyticsService {
    private final AnalyticsRepository analyticsRepository;
    private final RedisTemplate<String, AnalyticsResponseModel> redisTemplate;

    private static final String REDIS_KEY = "SHARE_LIST";

    private final RedisTemplate<String, String > redisTemplateString;
    private final EntityToResponseModelTransformer entityToResponseModelTransformer;
    @Override
    public List<AnalyticsResponseModel> getShareNameAnalytics(String shareName) {
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.getAnalyticsEntitiesByShareName(shareName, PageRequest.of(0, 20)));
    }

    @Override
    public AnalyticsResponseModel getShareLiveDataOnCache(String shareName) {
        return redisTemplate.opsForValue().get(shareName);
    }

    @Override
    public List<AnalyticsResponseModel> getAllCacheData() {
        String listOfShare = redisTemplateString.opsForValue().get(REDIS_KEY);
        List<String> shareList = List.of(listOfShare.split(":"));
        return shareList
                .stream()
                .map(s -> redisTemplate.opsForValue().get(s))
                .toList();
    }

}
