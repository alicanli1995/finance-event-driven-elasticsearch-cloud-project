package com.microservices.demo.elastic.query.service.service.impl;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import com.microservices.demo.elastic.model.index.impl.FinanceIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.demo.elastic.query.service.model.KafkaStreamsResponseModel;
import com.microservices.demo.elastic.query.service.model.assemblr.ElasticQueryResponseModelAssembler;
import com.microservices.demo.elastic.query.service.service.ElasticQueryService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.microservices.demo.elastic.query.service.util.Constants.CORRELATION_ID_HEADER;
import static com.microservices.demo.elastic.query.service.util.Constants.CORRELATION_ID_KEY;

@Log4j2
@Service
public class FinanceElasticQueryService implements ElasticQueryService {

    private final ElasticQueryResponseModelAssembler modelAssembler;

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;

    private final ElasticQueryClient<FinanceIndexModel> elasticQueryClient;

    private final WebClient.Builder webClientBuilder;

    public FinanceElasticQueryService(ElasticQueryResponseModelAssembler modelAssembler,
                                      ElasticQueryServiceConfigData elasticQueryServiceConfigData,
                                      ElasticQueryClient<FinanceIndexModel> elasticQueryClient,
                                      @Qualifier("webClientBuilder") WebClient.Builder webClientBuilder) {
        this.modelAssembler = modelAssembler;
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
        this.elasticQueryClient = elasticQueryClient;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public ElasticQueryResponseModel getDocumentById(String id) {
        log.info("Elasticsearch query for document with id {}", id);
        return modelAssembler.toModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public ElasticQueryServiceAnalyticsResponseModel getDocumentsByShareData(String c,String jwt) {
        log.info("Elasticsearch query for documents with share data: ".concat(c));
        var value =
                modelAssembler.toModels(elasticQueryClient.getIndexModelByFieldShareData(c));
        return ElasticQueryServiceAnalyticsResponseModel
                .builder()
                .queryResponseModels(value.get(0).getShareData().getC())
                .shareVolume(getVolumeLiveValue(c,jwt))
                .build();
    }

    private String getVolumeLiveValue(String c, String jwt) {
        ElasticQueryServiceConfigData.Query queryFromAnalyticsDatabase =
                elasticQueryServiceConfigData.getQueryFromAnalyticsDatabase();

        return Objects.requireNonNull(webClientBuilder
                .build()
                .method(HttpMethod.valueOf(queryFromAnalyticsDatabase.getMethod()))
                .uri(queryFromAnalyticsDatabase.getUri(), uriBuilder -> uriBuilder.build(c))
                .headers(h -> {
                    h.setBearerAuth(jwt);
                    h.set(CORRELATION_ID_HEADER, MDC.get(CORRELATION_ID_KEY));
                })
                .accept(MediaType.valueOf(queryFromAnalyticsDatabase.getAccept()))
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase()))
                )
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase()))
                )
                .bodyToMono(KafkaStreamsResponseModel.class)
                .log()
                .block()).getShareVolume();
    }

    @Override
    public List<ElasticQueryResponseModel> getAllDocuments() {
        log.info("Elasticsearch query for all documents");
        return modelAssembler.toModels(elasticQueryClient.getIndexModelForAll());
    }
}
