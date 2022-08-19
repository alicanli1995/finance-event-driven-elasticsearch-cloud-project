package com.microservices.demo.kafka.streams.service.runner;

public interface StreamsRunner<K, V> {
    void start();
    String getValueByKey(K key);
}
