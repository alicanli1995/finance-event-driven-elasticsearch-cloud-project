package com.microservices.demo.elastic.query.web.client.common.model;

import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryWebClientRequestModel {

    private String id;

    @NotNull
    private FinanceAvroDTO shareData;
}
