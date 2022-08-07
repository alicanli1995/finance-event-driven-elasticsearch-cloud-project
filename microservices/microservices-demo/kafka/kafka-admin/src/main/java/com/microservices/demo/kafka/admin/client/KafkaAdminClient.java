package com.microservices.demo.kafka.admin.client;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.RetryConfigData;
import com.microservices.demo.kafka.admin.exception.KafkaClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAdminClient {

    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;

    private final WebClient webClient;

    public void createTopics(){
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
        }catch (Throwable e){
            log.error("Error creating topics", e);
            throw new KafkaClientException("Reached max number of retries");
        }
        checkTopicsCreated();
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        var topicNames = kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating topics {}", topicNames);
        var newTopics = topicNames.stream()
                .map(topic -> new NewTopic(
                        topic.trim(),
                        kafkaConfigData.getNumOfPartitions(),
                        kafkaConfigData.getReplicationFactor()

                )).toList();
        return adminClient.createTopics(newTopics);
    }

    private Collection<TopicListing> getTopics(){
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        }
        catch (Throwable e){
            log.error("Error getting topics", e);
            throw new KafkaClientException("Reached max number of retries");
        }
        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        log.info("Getting topics");
        var topics = adminClient.listTopics().listings().get();
        if (Objects.nonNull(topics)){
            topics.forEach(topic -> log.info("Topic {}", topic.name()));
        }
        return topics;
    }

    public void checkTopicsCreated(){

        AtomicReference<Collection<TopicListing>> topics = new AtomicReference<>(getTopics());
        AtomicInteger retryCount = new AtomicInteger(1);
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier().intValue();
        AtomicReference<Long> sleepTime = new AtomicReference<>(retryConfigData.getInitialIntervalMs());
        kafkaConfigData.getTopicNamesToCreate().forEach(
                topic -> {
                    while (!isTopicCreated(topics,topic)){
                        checkMaxRetry(retryCount.getAndIncrement(), maxRetry);
                        sleep(sleepTime);
                        sleepTime.updateAndGet(v -> v * multiplier);
                        topics.set(getTopics());
                    }
                }
        );

    }

    public void checkSchemaRegistry(){
        AtomicInteger retryCount = new AtomicInteger(1);
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier().intValue();
        AtomicReference<Long> sleepTime = new AtomicReference<>(retryConfigData.getInitialIntervalMs());
        while (!isSchemaRegistryAvailable().is2xxSuccessful()){
            checkMaxRetry(retryCount.getAndIncrement(), maxRetry);
            sleep(sleepTime);
            sleepTime.updateAndGet(v -> v * multiplier);
        }
    }

    private HttpStatus isSchemaRegistryAvailable() {
        try {
            return webClient
                    .method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return Mono.just(response.statusCode());
                        } else {
                            return Mono.just(HttpStatus.SERVICE_UNAVAILABLE);
                        }
                    }).block();
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private void sleep(AtomicReference<Long> sleepTime) {
        try {
            Thread.sleep(sleepTime.get());
        }catch (InterruptedException e){
            log.error("Error sleeping", e);
        }
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry>maxRetry){
            throw new KafkaClientException("Reached max number of retries");
        }
    }

    private boolean isTopicCreated(AtomicReference<Collection<TopicListing>> topics, String topic) {
        if (Objects.isNull(topics.get())){
            return Boolean.FALSE;
        }
        return topics.get().stream().anyMatch(t -> t.name().equals(topic));
    }

}
