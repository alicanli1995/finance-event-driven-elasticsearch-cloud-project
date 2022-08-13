package com.microservices.demo.elastic.query.web.client.model;

import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
import lombok.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryWebClientResponseModel {

    private String id;

    private FinanceAvroDTO shareData;
    private ZonedDateTime createdAt;

    public String getCreatedAt() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm").format(createdAt);
    }

}
