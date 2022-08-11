package com.microservices.demo.elastic.query.client.util;

import com.microservices.demo.elastic.model.index.IndexModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ElasticQueryUtil<T extends IndexModel> {

    public Query getSearchQueryById(String id){
        return new NativeSearchQueryBuilder()
                .withIds(Collections.singleton(id))
                .build();
    }

    public Query getSearchQueryByFieldShareData(String field, String value){
        return new NativeSearchQueryBuilder()
                .withQuery(new BoolQueryBuilder()
                        .must(QueryBuilders.matchQuery(field, value)))
                .build();
    }

    public Query getSearchQueryForAll(){
        return new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .build();
    }


}
