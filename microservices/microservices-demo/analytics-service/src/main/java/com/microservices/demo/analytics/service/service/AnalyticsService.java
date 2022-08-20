package com.microservices.demo.analytics.service.service;

import com.microservices.demo.analytics.service.model.AnalyticsResponseModel;

import java.util.Optional;

public interface AnalyticsService {

    Optional<AnalyticsResponseModel> getShareNameAnalytics(String shareName);
}

