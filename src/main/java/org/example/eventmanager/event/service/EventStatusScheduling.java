package org.example.eventmanager.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.event.repository.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventStatusScheduling {

    private final EventRepository eventRepository;

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void eventStatusChange() {
        log.info("trying to update events status");

        long start = System.currentTimeMillis();
        int countEventStart = eventRepository.updateStartedEvents();
        int countEventFinish = eventRepository.updateFinishedEvents();
        long end = System.currentTimeMillis();

        log.info("all events were updated in {} seconds", (end - start) / 1000);
        log.info("count of event started {}", countEventStart);
        log.info("count of event finished {}", countEventFinish);
    }
}
