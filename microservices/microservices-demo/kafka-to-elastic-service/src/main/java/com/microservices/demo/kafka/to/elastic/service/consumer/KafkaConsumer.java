package com.microservices.demo.kafka.to.elastic.service.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.io.Serializable;
import java.util.List;

public interface KafkaConsumer<K extends Serializable, V extends SpecificRecordBase> {
    void receive(List<V> messages, List<String> keys , List<Integer> partitions , List<Long> offsets);
}
