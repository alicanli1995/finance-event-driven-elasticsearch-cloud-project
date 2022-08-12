package com.microservices.demo.elastic.query.service.model;

import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryRequestModel {

    private String id;
    @NotNull
    private FinanceAvroDTO shareData;
    private ZonedDateTime createdAt;

}
