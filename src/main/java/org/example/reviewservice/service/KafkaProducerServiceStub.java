package org.example.reviewservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class KafkaProducerServiceStub implements KafkaProducerService{





    public void sendMessage(String message) {
        return;

    }




}