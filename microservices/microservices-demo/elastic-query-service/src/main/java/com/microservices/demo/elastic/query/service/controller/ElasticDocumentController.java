package com.microservices.demo.elastic.query.service.controller;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.demo.elastic.query.service.security.FinanceQueryUser;
import com.microservices.demo.elastic.query.service.service.ElasticQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/documents")
@RequiredArgsConstructor
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;
    @Value("${server.port}")
    private String serverPort;

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
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @GetMapping
    public ResponseEntity<List<ElasticQueryResponseModel>> getAllDocuments(){
        List<ElasticQueryResponseModel> response = elasticQueryService.getAllDocuments();
        log.info("Elastic returned {} of documents", response.size());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails userName) {
             username = userName.getUsername();

        } else {
             username = principal.toString();
        }
        log.info("User {} is accessing the service on port {}", username, serverPort);

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
    @PreAuthorize("hasPermission(#id,'ElasticQueryResponseModel' ,'READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryResponseModel> getDocumentsById(@PathVariable @NotNull @Validated String id){
        ElasticQueryResponseModel elasticQueryServiceResponseModel =elasticQueryService.getDocumentById(id);
        log.debug("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(elasticQueryServiceResponseModel);
    }


    @PreAuthorize("hasRole('APP_USER_ROLE') || hasRole('APP_SUPER_USER_ROLE') || hasAuthority('SCOPE_APP_USER_ROLE')")
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @Operation(summary = "Get elastic document by text.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ElasticQueryResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/get-document-by-text")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceAnalyticsResponseModel>
    getDocumentByText(@RequestBody @Valid ElasticQueryRequestModel elasticQueryServiceRequestModel,
                      @AuthenticationPrincipal FinanceQueryUser principal,
                      @RegisteredOAuth2AuthorizedClient("keycloak")
                      OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        log.info("User {} querying documents for text {}", principal.getUsername(),
                elasticQueryServiceRequestModel.getShareData().getC());
        ElasticQueryServiceAnalyticsResponseModel response =
                elasticQueryService.getDocumentsByShareData(elasticQueryServiceRequestModel.getShareData().getC(),
                        oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        return ResponseEntity.ok(response);
    }

}
