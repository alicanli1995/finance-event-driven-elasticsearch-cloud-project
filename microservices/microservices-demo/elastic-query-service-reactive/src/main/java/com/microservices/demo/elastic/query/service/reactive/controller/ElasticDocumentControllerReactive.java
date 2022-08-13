package com.microservices.demo.elastic.query.service.reactive.controller;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.reactive.service.ElasticQueryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
public class ElasticDocumentControllerReactive {
    private final ElasticQueryService elasticQueryService;
    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentControllerReactive.class);


    @PostMapping(value = "/get-doc-by-text",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ElasticQueryResponseModel> getDocumentByText(
            @RequestBody @Valid ElasticQueryRequestModel requestModel) {
        Flux<ElasticQueryResponseModel> response =
                elasticQueryService.getDocumentByShareData(requestModel.getShareData().getC());

        response = response.log();

        LOG.info("Returning from query reactive service for text {}!", requestModel.getShareData().getC());

        return response;
    }

}
