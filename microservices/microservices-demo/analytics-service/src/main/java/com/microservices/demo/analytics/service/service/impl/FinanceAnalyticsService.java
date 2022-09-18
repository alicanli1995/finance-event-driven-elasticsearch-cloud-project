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

    private static final String REDIS_SEPARATOR = ":";

    private final RedisTemplate<String, String > redisTemplateString;
    private final EntityToResponseModelTransformer entityToResponseModelTransformer;
    @Override
    public List<AnalyticsResponseModel> getShareNameAnalytics(String shareName) {
        log.info("Getting analytics for share name {}", shareName);
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.
                        getAnalyticsEntitiesByShareNameAndOrderByCreatedDate(shareName, PageRequest.of(0, 20))
                        .stream()
                        .sorted((a, b) -> b.getCreatedDate().compareTo(a.getCreatedDate()))
                        .toList());
    }

    @Override
    public AnalyticsResponseModel getShareLiveDataOnCache(String shareName) {
        log.info("Getting live data for share name {}", shareName);
        return redisTemplate.opsForValue().get(shareName);
    }

    @Override
    public List<AnalyticsResponseModel> getAllCacheData() {
        log.info("Getting all live data");
        String listOfShare = redisTemplateString.opsForValue().get(REDIS_KEY);
        assert listOfShare != null;
        List<String> shareList = List.of(listOfShare.split(REDIS_SEPARATOR));
        return shareList
                .stream()
                .map(s -> redisTemplate.opsForValue().get(s))
                .toList();
    }

}
