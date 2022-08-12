package com.microservices.demo.elastic.model.index.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceAvroDTO {
    private String dailyChangePercentage;
    private String dailyChange;
    private String c;
    private String last;
    private String dailyVolume;
    private String previousDayClose;
    private String description;
}
