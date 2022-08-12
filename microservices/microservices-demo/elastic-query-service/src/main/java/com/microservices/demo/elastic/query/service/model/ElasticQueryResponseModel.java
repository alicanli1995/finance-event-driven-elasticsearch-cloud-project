package com.microservices.demo.elastic.query.service.model;

import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryResponseModel extends RepresentationModel<ElasticQueryResponseModel> {
    private String id;
    private FinanceAvroDTO shareData;
    private ZonedDateTime createdAt;
}
