package org.example.eventmanager.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.event.dto.EventKafkaMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventKafkaProducer {
    private final KafkaTemplate<Long, EventKafkaMessage> kafkaTemplate;

    public void sendEventChanges(EventKafkaMessage kafkaMessage) {
        log.info("send event changes: {}", kafkaMessage);
        CompletableFuture<SendResult<Long, EventKafkaMessage>> send =
                kafkaTemplate.send("event-changes", kafkaMessage.ownerId(), kafkaMessage);

        send.thenAccept(sendResult ->
                log.info("message successfully sent to kafka: {}", sendResult.getProducerRecord()));
    }
}
