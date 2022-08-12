package com.microservices.demo.elastic.query.client.repository;

import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceElasticSearchQueryRepository extends ElasticsearchRepository<FinanceIndexModel,String > {
    List<FinanceIndexModel> findByShareDataCIn(String c);

}
