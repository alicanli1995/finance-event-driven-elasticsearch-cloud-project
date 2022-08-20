package com.microservices.demo.elastic.query.service.service;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryResponseModel getDocumentById(String id);
    ElasticQueryServiceAnalyticsResponseModel getDocumentsByShareData(String c,String jwt);
    List<ElasticQueryResponseModel> getAllDocuments();

}
