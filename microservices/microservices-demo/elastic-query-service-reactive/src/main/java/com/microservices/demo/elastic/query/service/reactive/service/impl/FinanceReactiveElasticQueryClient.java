package com.microservices.demo.elastic.query.service.reactive.service.impl;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.query.service.reactive.repository.ElasticQueryRepository;
import com.microservices.demo.elastic.query.service.reactive.service.ReactiveElasticQueryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceReactiveElasticQueryClient implements ReactiveElasticQueryClient<FinanceIndexModel> {

    private final ElasticQueryRepository elasticQueryRepository;

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;

    @Override
    public Flux<FinanceIndexModel> getIndexModelByText(String c) {
        log.info("Getting data from elasticsearch for text {}", c);
        return elasticQueryRepository
                .findByShareDataCIn(c)
                .delayElements(Duration.ofMillis(elasticQueryServiceConfigData.getBackPressureDelayMs()));
    }
}
