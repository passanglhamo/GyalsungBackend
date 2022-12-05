package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.dto.CoachBus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddToQueue {
    private final KafkaTemplate<?, ?> kafkaTemplate;

    public void addToQueue(String topic, CoachBus coachBus) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message<String> message = MessageBuilder
                .withPayload(mapper.writeValueAsString(
                        coachBus
                ))
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        kafkaTemplate.send(message);
    }
}
