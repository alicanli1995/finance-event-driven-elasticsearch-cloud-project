package com.microservices.demo.elastic.query.service.controller;

import com.microservices.demo.elastic.query.service.model.ElasticQueryRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.service.ElasticQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin
@RequestScope
@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;

    @Operation(summary = "Search documents in Elasticsearch on All Documents",
            description = "Search documents in Elasticsearch on All Documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content =
                    @Content(schema =
                    @Schema(implementation = ElasticQueryResponseModel.class))),
    @ApiResponse(responseCode = "400", description = "Bad Request"),
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseBody
    @GetMapping
    public ResponseEntity<List<ElasticQueryResponseModel>> getAllDocuments(){
        List<ElasticQueryResponseModel> response = elasticQueryService.getAllDocuments();
        log.info("Elastic returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search documents in Elasticsearch with Document ID",
            description = "Search documents in Elasticsearch with Document ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content =
            @Content(schema =
            @Schema(implementation = ElasticQueryResponseModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryResponseModel> getDocumentsById(@PathVariable @NotNull @Validated String id){
        ElasticQueryResponseModel elasticQueryServiceResponseModel =elasticQueryService.getDocumentById(id);
        log.debug("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(elasticQueryServiceResponseModel);
    }

    @Operation(summary = "Search documents in Elasticsearch with ShareName",
            description = "Search documents in Elasticsearch with ShareName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content =
            @Content(schema =
            @Schema(implementation = ElasticQueryResponseModel.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @ResponseBody
    @PostMapping("/{shareData}")
    public ResponseEntity<List<ElasticQueryResponseModel>> getDocumentsByShareData(
            @RequestBody ElasticQueryRequestModel elasticQueryRequestModel){
        List<ElasticQueryResponseModel> response =
                elasticQueryService.getDocumentsByShareData(elasticQueryRequestModel.getShareData().getC());
        log.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

}
