package com.microservices.demo.elastic.query.service.reactive.service.impl;


import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.query.service.common.converter.ElasticToResponseModelConverter;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.reactive.service.ElasticQueryService;
import com.microservices.demo.elastic.query.service.reactive.service.ReactiveElasticQueryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceElasticQueryService implements ElasticQueryService {

    private final ReactiveElasticQueryClient<FinanceIndexModel> reactiveElasticQueryClient;

    private final ElasticToResponseModelConverter elasticToResponseModelTransformer;

    @Override
    public Flux<ElasticQueryResponseModel> getDocumentByShareData(String text) {
        return reactiveElasticQueryClient
                .getIndexModelByText(text)
                .map(elasticToResponseModelTransformer::convert);
    }
}
