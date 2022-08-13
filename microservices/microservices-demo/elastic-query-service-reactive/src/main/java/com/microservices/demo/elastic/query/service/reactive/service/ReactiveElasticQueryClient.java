package com.microservices.demo.elastic.query.service.reactive.service;

import com.microservices.demo.elastic.model.index.IndexModel;
import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import reactor.core.publisher.Flux;

public interface ReactiveElasticQueryClient<T extends IndexModel> {
    Flux<FinanceIndexModel> getIndexModelByText(String text);
}
