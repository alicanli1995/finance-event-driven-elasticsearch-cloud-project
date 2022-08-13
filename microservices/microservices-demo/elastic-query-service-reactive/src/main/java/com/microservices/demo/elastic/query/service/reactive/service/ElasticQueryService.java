package com.microservices.demo.elastic.query.service.reactive.service;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticQueryService {
    Flux<ElasticQueryResponseModel> getDocumentByShareData(String text);

}
