package com.microservices.demo.elastic.query.web.client.exception;

public class ElasticQueryWebClientException extends RuntimeException {
    public ElasticQueryWebClientException(String message) {
        super(message);
    }
    public ElasticQueryWebClientException(String message, Throwable cause) {
        super(message, cause);
    }
    public ElasticQueryWebClientException() {
        super();
    }
}
