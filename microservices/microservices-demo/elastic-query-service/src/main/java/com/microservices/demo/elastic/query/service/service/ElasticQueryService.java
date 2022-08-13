package com.microservices.demo.elastic.query.service.service;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryResponseModel getDocumentById(String id);
    List<ElasticQueryResponseModel> getDocumentsByShareData(String c);
    List<ElasticQueryResponseModel> getAllDocuments();

}
