package org.example.reviewservice.exception;

public class KafkaProducerException extends IllegalArgumentException {
    public KafkaProducerException(String message) {
        super(message);
    }
    public KafkaProducerException(String message, Throwable cause) {
        super(message, cause);
    }
}
