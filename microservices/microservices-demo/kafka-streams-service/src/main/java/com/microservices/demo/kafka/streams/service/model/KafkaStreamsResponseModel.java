package com.microservices.demo.kafka.streams.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaStreamsResponseModel {

    private String shareName;
    private String shareVolume;


}
