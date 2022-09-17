package com.microservices.demo.analytics.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponseModel implements Serializable {
    private UUID id;
    private String shareName;
    private String shareVolume;
    private LocalDateTime recordTime;
}
