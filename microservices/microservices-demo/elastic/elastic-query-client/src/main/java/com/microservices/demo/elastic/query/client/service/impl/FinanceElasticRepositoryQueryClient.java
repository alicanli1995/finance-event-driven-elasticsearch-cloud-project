package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.common.util.CollectionsUtil;
import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.query.client.repository.FinanceElasticSearchQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class FinanceElasticRepositoryQueryClient implements ElasticQueryClient<FinanceIndexModel> {

    private final FinanceElasticSearchQueryRepository financeElasticSearchQueryRepository;

    @Override
    public FinanceIndexModel getIndexModelById(String id) {
        return financeElasticSearchQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No result found for id: " + id));
    }

    @Override
    public List<FinanceIndexModel> getIndexModelByFieldShareData(String field) {
        log.info("Elasticsearch query for documents with share data: ".concat(field));
        return financeElasticSearchQueryRepository.findByShareDataCIn(field);
    }

    @Override
    public List<FinanceIndexModel> getIndexModelForAll() {
        return CollectionsUtil.getInstance()
                .getListFromIterable(financeElasticSearchQueryRepository.findAll());
    }
}
