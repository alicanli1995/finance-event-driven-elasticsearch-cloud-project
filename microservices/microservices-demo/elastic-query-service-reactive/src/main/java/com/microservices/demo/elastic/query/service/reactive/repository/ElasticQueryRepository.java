package com.microservices.demo.elastic.query.service.reactive.repository;

import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticQueryRepository extends ReactiveCrudRepository<FinanceIndexModel, String> {

    Flux<FinanceIndexModel> findByShareDataCIn(String c);

}
