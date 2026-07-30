package org.example.reviewservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class KafkaProducerServiceReal implements KafkaProducerService{

    @Value("${spring.kafka.topic}")
    private String TOPIC;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerServiceReal(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);

    }




}