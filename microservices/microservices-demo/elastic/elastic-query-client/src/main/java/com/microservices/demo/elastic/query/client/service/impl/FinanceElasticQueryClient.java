package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.config.ElasticQueryConfigData;
import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.client.util.ElasticQueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class FinanceElasticQueryClient implements ElasticQueryClient<FinanceIndexModel> {

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<FinanceIndexModel>  elasticQueryUtil;


    @Override
    public FinanceIndexModel getIndexModelById(String id) {
        var query = elasticQueryUtil.getSearchQueryById(id);
        var result = elasticsearchOperations.searchOne(query, FinanceIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        if (Objects.isNull(result)) {
            throw new ElasticQueryClientException("No result found for id: " + id);
        }
        return result.getContent();
    }

    @Override
    public List<FinanceIndexModel> getIndexModelByFieldShareData(String c) {
        var query = elasticQueryUtil.getSearchQueryByFieldShareData(elasticQueryConfigData.getShareData(), c);
        var searchResult = elasticsearchOperations.search(query, FinanceIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        return searchResult.get().map(SearchHit::getContent).toList();
    }

    @Override
    public List<FinanceIndexModel> getIndexModelForAll() {
        var query = elasticQueryUtil.getSearchQueryForAll();
        var result = elasticsearchOperations.search(query, FinanceIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        if (result.isEmpty()) {
            throw new ElasticQueryClientException("No result found for all");
        }
        return result.get().map(SearchHit::getContent).toList();
    }
}
