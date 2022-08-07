package com.finance.stream.api;

import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.FinanceAvroModel;
import com.microservices.demo.kafka.avro.model.Share;
import com.microservices.demo.kafka.producer.config.service.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = {MockSerdeConfig.class}, properties = {"spring.main.allow-bean-definition-overriding=true"})
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class FinanceStreamAppTests {
    @Autowired
    private KafkaProducer<String, FinanceAvroModel> kafkaProducer;

    @MockBean
    private KafkaAdminClient kafkaAdminClient;

    @Test
    void testKafka() {

        doNothing().when(kafkaAdminClient).checkSchemaRegistry();

        FinanceAvroModel financeAvroModel = FinanceAvroModel.newBuilder()
                .setId("74772abc-0a62-47ca-9210-24437c26a6c1")
                .setCreatedAt(new Date().getTime())
                .setShareData(Share.newBuilder()
                        .setPreviousDayClose("TEST")
                        .setDailyVolume("TEST")
                        .setDailyChangePercentage("TEST")
                        .setLast("TEST")
                        .setDescription("TEST")
                        .setDailyChange("TEST")
                        .setC("TEST")
                        .build())
                .build();
        kafkaProducer.send("finance-topic", financeAvroModel.getId(), financeAvroModel);
    }

}
