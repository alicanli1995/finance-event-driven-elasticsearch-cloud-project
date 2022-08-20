package com.microservices.demo.analytics.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponseModel {
    private UUID id;
    private String shareName;
    private String shareVolume;
}
