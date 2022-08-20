package com.microservices.demo.kafka.streams.service.runner.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaStreamsConfigData;
import com.microservices.demo.kafka.avro.model.FinanceAnalyticsAvroModel;
import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Component
public class KafkaStreamsRunner implements StreamsRunner<String, Long> {
    private final KafkaStreamsConfigData kafkaStreamsConfigData;

    private final KafkaConfigData kafkaConfigData;

    private final Properties streamsConfiguration;

    private KafkaStreams kafkaStreams;

    private HashMap<String, String> liveShareValues = new HashMap<>();



    public KafkaStreamsRunner(KafkaStreamsConfigData kafkaStreamsConfig,
                              KafkaConfigData kafkaConfig,
                              @Qualifier("streamConfiguration") Properties streamsConfiguration) {
        this.kafkaStreamsConfigData = kafkaStreamsConfig;
        this.kafkaConfigData = kafkaConfig;
        this.streamsConfiguration = streamsConfiguration;
    }

    @Override
    public void start() {
        final Map<String, String> serdeConfig = Collections.singletonMap(
                kafkaConfigData.getSchemaRegistryUrlKey(),
                kafkaConfigData.getSchemaRegistryUrl());

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        final KStream<String, FinanceAvroModel> financeAvroModelKStream =
                getFinanceAvroModelKStream(serdeConfig, streamsBuilder);

        createTopology(financeAvroModelKStream, serdeConfig);

        startStreaming(streamsBuilder);
    }

    @Override
    public String getValueByKey(String word) {
        if (Objects.nonNull(kafkaStreams) && kafkaStreams.state().equals(KafkaStreams.State.RUNNING)) {
            return liveShareValues.get(word);
        }
        return "Server Error";
    }



    @PreDestroy
    public void close() {
        if (Objects.nonNull(kafkaStreams)) {
            kafkaStreams.close();
            log.info("Kafka streaming closed!");
        }
    }


    private void startStreaming(StreamsBuilder streamsBuilder) {
        final Topology topology = streamsBuilder.build();
        log.info("Defined topology: {}", topology.describe());
        kafkaStreams = new KafkaStreams(topology, streamsConfiguration);
        kafkaStreams.start();
        log.info("Kafka streaming started..");
    }

    private void createTopology(KStream<String, FinanceAvroModel> stringKStream,
                                Map<String, String> serdeConfig) {

        Serde<FinanceAnalyticsAvroModel> analyticsModel = getSerdeAnalyticsModel(serdeConfig);

        stringKStream
                .mapValues(value -> (value.getShareData().getC() + ": " + (value.getShareData().getLast())))
                .map(mapToAnalyticsModel())
                .to(kafkaStreamsConfigData.getOutputTopicName(),
                        Produced.with(Serdes.String(), analyticsModel));
    }

    private KeyValueMapper<String, String , KeyValue< String, FinanceAnalyticsAvroModel>> mapToAnalyticsModel() {
        return (word, count) -> {
            var st = Arrays.stream(count.split(":")).toList();
            log.info("Sending to topic {}, share {} - value {}",
                    kafkaStreamsConfigData.getOutputTopicName(), st.get(0), count);

            liveShareValues.put(st.get(0), count);

            return new KeyValue<>(st.get(0), FinanceAnalyticsAvroModel
                    .newBuilder()
                    .setShare(word)
                    .setShareVolume(count)
                    .setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build());
        };
    }

    private Serde<FinanceAnalyticsAvroModel> getSerdeAnalyticsModel(Map<String, String> serdeConfig) {
        Serde<FinanceAnalyticsAvroModel> financeAnalyticsAvroModelSpecificAvroSerde = new SpecificAvroSerde<>();
        financeAnalyticsAvroModelSpecificAvroSerde.configure(serdeConfig, false);
        return financeAnalyticsAvroModelSpecificAvroSerde;
    }

    private KStream<String, FinanceAvroModel> getFinanceAvroModelKStream(Map<String, String> serdeConfig,
                                                                         StreamsBuilder streamsBuilder) {
        final Serde<FinanceAvroModel> financeAvroModelSpecificAvroSerde = new SpecificAvroSerde<>();
        financeAvroModelSpecificAvroSerde.configure(serdeConfig, false);
        return streamsBuilder.stream(kafkaStreamsConfigData.getInputTopicName(), Consumed.with(Serdes.String(),
                financeAvroModelSpecificAvroSerde));
    }


}
