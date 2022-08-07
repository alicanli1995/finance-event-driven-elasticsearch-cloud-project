package com.microservices.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "finance-share")
public class FinanceDataStreamConfig {
    private List<String> shareList;
    private String urlAppend;
}
