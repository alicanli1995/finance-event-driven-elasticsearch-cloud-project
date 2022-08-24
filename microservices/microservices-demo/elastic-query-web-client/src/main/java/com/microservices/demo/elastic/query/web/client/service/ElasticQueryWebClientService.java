package com.microservices.demo.elastic.query.web.client.service;


import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.microservices.demo.elastic.query.web.client.model.LiveValuesResponse;

import java.util.List;

public interface ElasticQueryWebClientService {
    ElasticQueryWebClientAnalyticsResponseModel getShareByC(ElasticQueryWebClientRequestModel model);

    LiveValuesResponse getLiveLiveValues(List<String> shareList);
}
