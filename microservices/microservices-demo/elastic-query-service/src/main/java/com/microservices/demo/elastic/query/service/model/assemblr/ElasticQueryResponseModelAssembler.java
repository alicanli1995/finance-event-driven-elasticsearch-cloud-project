package com.microservices.demo.elastic.query.service.model.assemblr;

import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.query.service.common.converter.ElasticToResponseModelConverter;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.controller.ElasticDocumentController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Component
public class ElasticQueryResponseModelAssembler extends
        RepresentationModelAssemblerSupport<FinanceIndexModel, ElasticQueryResponseModel> {

    private final ElasticToResponseModelConverter elasticToResponseModelConverter;

    public ElasticQueryResponseModelAssembler(ElasticToResponseModelConverter elasticToResponseModelConverter) {
        super(ElasticDocumentController.class, ElasticQueryResponseModel.class);
        this.elasticToResponseModelConverter = elasticToResponseModelConverter;
    }


    @Override
    public ElasticQueryResponseModel toModel(FinanceIndexModel entity) {
        ElasticQueryResponseModel responseModel = elasticToResponseModelConverter.convert(entity);
        responseModel.add(
                linkTo(methodOn(ElasticDocumentController.class)
                        .getDocumentsById(entity.getId()))
                        .withSelfRel());
        responseModel.add(
                linkTo(ElasticDocumentController.class)
                        .withRel("documents"));

        return responseModel;

    }

    public List<ElasticQueryResponseModel> toModels(List<FinanceIndexModel> financeIndexModels) {
        return financeIndexModels
                .stream()
                .map(this::toModel)
                .toList();
    }
}
