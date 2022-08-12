package com.microservices.demo.elastic.query.service.service.impl;

import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.model.assemblr.ElasticQueryResponseModelAssembler;
import com.microservices.demo.elastic.query.service.service.ElasticQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FinanceElasticQueryService implements ElasticQueryService {

    private final ElasticQueryResponseModelAssembler modelAssembler;

    private final ElasticQueryClient<FinanceIndexModel> elasticQueryClient;

    @Override
    public ElasticQueryResponseModel getDocumentById(String id) {
        log.info("Elasticsearch query for document with id {}", id);
        return modelAssembler.toModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryResponseModel> getDocumentsByShareData(String c) {
        log.info("Elasticsearch query for documents with share data: ".concat(c));
        return modelAssembler.toModels(elasticQueryClient.getIndexModelByFieldShareData(c));
    }

    @Override
    public List<ElasticQueryResponseModel> getAllDocuments() {
        log.info("Elasticsearch query for all documents");
        return modelAssembler.toModels(elasticQueryClient.getIndexModelForAll());
    }
}
