package com.microservices.demo.elastic.query.web.client.service;

import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;

import java.util.List;

public interface ElasticQueryWebClientService {
    List<ElasticQueryWebClientResponseModel> getShareByC(ElasticQueryWebClientRequestModel model);
}
