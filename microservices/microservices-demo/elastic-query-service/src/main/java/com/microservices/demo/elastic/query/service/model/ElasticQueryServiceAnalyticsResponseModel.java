package com.microservices.demo.elastic.query.service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryServiceAnalyticsResponseModel {
    private String queryResponseModels;
    private String shareVolume;
}
