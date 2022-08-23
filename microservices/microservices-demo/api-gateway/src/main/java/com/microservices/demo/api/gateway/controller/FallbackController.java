package com.microservices.demo.api.gateway.controller;

import com.microservices.demo.api.gateway.model.AnalyticsDataFallbackModel;
import com.microservices.demo.api.gateway.model.QueryServiceFallbackModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {


    @PostMapping("/query-fallback")
    public ResponseEntity<QueryServiceFallbackModel> queryServiceFallback() {
        log.info("Returning fallback result for elastic-query-service!");
        return ResponseEntity.ok(QueryServiceFallbackModel.builder()
                .fallbackMessage("Fallback result for elastic-query-service!")
                .build());
    }

    @PostMapping("/analytics-fallback")
    public ResponseEntity<AnalyticsDataFallbackModel> analyticsServiceFallback() {
        log.info("Returning fallback result for analytics-service!");
        return ResponseEntity.ok(AnalyticsDataFallbackModel.builder()
                .shareVolume("Non Data !")
                .build());
    }


    @PostMapping("/streams-fallback")
    public ResponseEntity<AnalyticsDataFallbackModel> streamsServiceFallback() {
        log.info("Returning fallback result for kafka-streams-service!");
        return ResponseEntity.ok(AnalyticsDataFallbackModel.builder()
                .shareVolume("Non Data !")
                .build());
    }


}
