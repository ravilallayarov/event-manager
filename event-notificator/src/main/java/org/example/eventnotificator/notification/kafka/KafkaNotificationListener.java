package org.example.eventnotificator.notification.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventnotificator.notification.dto.EventKafkaMessage;
import org.example.eventnotificator.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaNotificationListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "event-changes", containerFactory = "factory")
    public void eventNotificationListener(EventKafkaMessage message) {
        log.info("reading message from kafka: {}", message);
        notificationService.saveEventNotification(message);
    }
}
