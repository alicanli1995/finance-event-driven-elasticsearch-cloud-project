package com.microservices.demo.analytics.service.service;

import com.microservices.demo.analytics.service.model.AnalyticsResponseModel;

import java.util.List;


public interface AnalyticsService {

    List<AnalyticsResponseModel> getShareNameAnalytics(String shareName);

    AnalyticsResponseModel getShareLiveDataOnCache(String shareName);
    List<AnalyticsResponseModel> getAllCacheData();
}

