package com.microservices.demo.elastic.query.web.client.service;


import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientAnalyticsResponseModel;

public interface ElasticQueryWebClientService {
    ElasticQueryWebClientAnalyticsResponseModel getShareByC(ElasticQueryWebClientRequestModel model);
}
