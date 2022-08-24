package com.microservices.demo.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.model.index.model.FinanceAvroDTO;
import com.microservices.demo.elastic.query.web.client.common.exception.ElasticQueryWebClientException;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.microservices.demo.elastic.query.web.client.model.LiveValuesResponse;
import com.microservices.demo.elastic.query.web.client.service.ElasticQueryWebClientService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ElasticQueryWebClientServiceImpl implements ElasticQueryWebClientService {
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_KEY = "correlationID";
    private final WebClient.Builder webClientBuilder;
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;


    public ElasticQueryWebClientServiceImpl(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                                            ElasticQueryWebClientConfigData elasticQueryWebClientConfigData) {
        this.webClientBuilder = webClientBuilder;
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData;
    }


    @Override
    public ElasticQueryWebClientAnalyticsResponseModel getShareByC(ElasticQueryWebClientRequestModel model) {
        log.info("ElasticQueryWebClientServiceImpl.getShareByC()");
        return getWebClient(model)
                .bodyToMono(ElasticQueryWebClientAnalyticsResponseModel.class)
                .block();
    }

    @Override
    public LiveValuesResponse getLiveLiveValues(List<String> shareList) {
        Map<String , BigDecimal> liveValuesMap = new HashMap<>();
        shareList.forEach(share -> {
            var response = getShareByC(ElasticQueryWebClientRequestModel.builder()
                    .shareData(FinanceAvroDTO.builder().c(share).build())
                    .build());
            liveValuesMap.put(share, new BigDecimal(response.getShareVolume()));
        });
        return LiveValuesResponse.builder().liveValues(liveValuesMap).build();
    }


    private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequestModel requestModel) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(elasticQueryWebClientConfigData.getQueryByText().getMethod()))
                .uri(elasticQueryWebClientConfigData.getQueryByText().getUri())
                .accept(MediaType.valueOf(elasticQueryWebClientConfigData.getQueryByText().getAccept()))
                .header(CORRELATION_ID_HEADER, MDC.get(CORRELATION_ID_KEY))
                .body(BodyInserters.fromPublisher(Mono.just(requestModel), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated!")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(
                                new ElasticQueryWebClientException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())));
    }



    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
