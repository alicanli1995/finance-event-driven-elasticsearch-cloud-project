package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.FinanceElasticsearchIndexRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class FinanceElasticRepositoryIndexClient implements ElasticIndexClient<FinanceIndexModel> {

    private final FinanceElasticsearchIndexRepository financeElasticsearchIndexRepository;

    @Override
    public List<String> save(List<FinanceIndexModel> documents) {
        var savingQueries = (List<FinanceIndexModel>) financeElasticsearchIndexRepository.saveAll(documents);
        var result = savingQueries.stream().map(FinanceIndexModel::getId).toList();
        log.info("Saved {} documents", savingQueries.size());
        return result;
    }
}
