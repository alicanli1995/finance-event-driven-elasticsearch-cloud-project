package com.microservices.demo.elastic.query.web.client.config;


import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@LoadBalancerClient(name = "elastic-query-service", configuration = InstanceSupplierConfig.class)
public class WebClientConfig {
    private final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;

    @Value("${security.default-client-registration-id}")
    private String defaultClientRegistrationId;


    public WebClientConfig(ElasticQueryWebClientConfigData webClientConfigData) {
        this.elasticQueryWebClientConfigData = webClientConfigData.getWebClient();
    }

    @Bean("webClientBuilder")
    @LoadBalanced
    WebClient.Builder webClientBuilder(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository
    ) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrationRepository,
                        oAuth2AuthorizedClientRepository);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        oauth2.setDefaultClientRegistrationId(defaultClientRegistrationId);

        return WebClient.builder()
                .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticQueryWebClientConfigData.getContentType())
                .defaultHeader(HttpHeaders.ACCEPT, elasticQueryWebClientConfigData.getAcceptType())
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .apply(oauth2.oauth2Configuration())
                .codecs(clientCodecConfigurer ->
                        clientCodecConfigurer
                                .defaultCodecs()
                                .maxInMemorySize(elasticQueryWebClientConfigData.getMaxInMemorySize()));
    }

    private HttpClient getHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, elasticQueryWebClientConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(elasticQueryWebClientConfigData.getReadTimeoutMs(),
                            TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(
                            new WriteTimeoutHandler(elasticQueryWebClientConfigData.getWriteTimeoutMs(),
                                    TimeUnit.MILLISECONDS));
                });
    }
}
