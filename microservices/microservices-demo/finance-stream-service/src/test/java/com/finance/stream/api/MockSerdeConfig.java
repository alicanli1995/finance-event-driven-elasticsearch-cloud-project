package com.finance.stream.api;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaProducerConfigData;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class MockSerdeConfig {


    private final KafkaConfigData kafkaConfigData;

    private final KafkaProducerConfigData kafkaProducerConfigData;


    public MockSerdeConfig(KafkaConfigData configData, KafkaProducerConfigData producerConfigData) {
        this.kafkaConfigData = configData;
        this.kafkaProducerConfigData = producerConfigData;
    }

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializerClass());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize() *
                kafkaProducerConfigData.getBatchSizeBoostFactor());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());
        return props;
    }

    @Bean
    MockSchemaRegistryClient schemaRegistryClient() throws IOException, RestClientException {
        MockSchemaRegistryClient mockSchemaRegistryClient = new MockSchemaRegistryClient();

        AvroSchema avroSchema = new AvroSchema
                (
                        "{\n" +
                        "  \"namespace\": \"com.microservices.demo.kafka.avro.model\",\n" +
                        "  \"type\": \"record\",\n" +
                        "  \"name\": \"FinanceAvroModel\",\n" +
                        "  \"fields\": [\n" +
                        "    {\n" +
                        "      \"name\": \"id\",\n" +
                        "      \"type\": {\n" +
                        "        \"type\": \"string\",\n" +
                        "        \"logicalType\": \"uuid\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"shareData\",\n" +
                        "      \"type\": {\n" +
                        "        \"name\": \"Share\",\n" +
                        "        \"type\": \"record\",\n" +
                        "        \"fields\": [\n" +
                        "          {\n" +
                        "            \"name\": \"dailyChangePercentage\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"name\": \"dailyChange\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"name\": \"c\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"name\": \"last\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"name\": \"dailyVolume\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"name\": \"previousDayClose\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          },\n" +
                        "          {\n" +
                        "            \"name\": \"description\",\n" +
                        "            \"type\": \"string\"\n" +
                        "          }\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"createdAt\",\n" +
                        "      \"type\": [\"null\", \"long\"],\n" +
                        "      \"logicalType\": [\"null\", \"date\"]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}");
        mockSchemaRegistryClient.register("finance-topic-value", avroSchema);

        return mockSchemaRegistryClient;
    }

    @Bean
    KafkaAvroSerializer kafkaAvroSerializer() throws IOException, RestClientException {
        return new KafkaAvroSerializer(schemaRegistryClient());
    }


    @Bean
    DefaultKafkaProducerFactory producerFactory() throws IOException, RestClientException {
        return new DefaultKafkaProducerFactory(
                producerConfig(),
                new StringSerializer(),
                kafkaAvroSerializer()
        );
    }

    @Bean
    public KafkaTemplate kafkaTemplate() throws IOException, RestClientException {
        return new KafkaTemplate<>(producerFactory());
    }



}
