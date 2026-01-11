package org.example.eventmanager.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.event.dto.EventKafkaMessage;
import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.event.entity.EventStatus;
import org.example.eventmanager.event.entity.RegistrationEntity;
import org.example.eventmanager.event.repository.CustomEventRepository;
import org.example.eventmanager.security.entity.UserEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventStatusScheduling {

    private final CustomEventRepository customEventRepository;
    private final EventKafkaProducer eventKafkaProducer;

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void eventStatusChange() {
        log.info("trying to update events status");

        long start = System.currentTimeMillis();
        List<EventEntity> startedEvents = customEventRepository.updateStartedEvents();
        List<EventEntity> finishedEvents = customEventRepository.updateFinishedEvents();
        long end = System.currentTimeMillis();

        log.info("all events were updated in {} seconds", (end - start) / 1000);
        log.info("count of event started {}", startedEvents.size());
        log.info("count of event finished {}", finishedEvents.size());

        for(EventEntity startedEvent: startedEvents) {
            EventKafkaMessage message = createMessage(
                    startedEvent, EventStatus.WAIT_START.name(), EventStatus.STARTED.name());
            eventKafkaProducer.sendEventChanges(message);
        }

        for(EventEntity finishedEvent: finishedEvents) {
            EventKafkaMessage message = createMessage(
                    finishedEvent, EventStatus.STARTED.name(), EventStatus.FINISHED.name());
            eventKafkaProducer.sendEventChanges(message);
        }
    }

    private EventKafkaMessage createMessage(EventEntity event, String oldValue, String newValue) {
        return new EventKafkaMessage(
                event.getId(),
                null,
                event.getUserEntity().getId(),
                List.of(new EventKafkaMessage.ModifiedField("eventStatus", oldValue, newValue)),
                event.getRegistrations().stream().map(RegistrationEntity::getUser).map(UserEntity::getId).toList());
    }
}
