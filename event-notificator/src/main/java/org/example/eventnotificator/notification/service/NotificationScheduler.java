package org.example.eventnotificator.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventnotificator.notification.entity.NotificationEntity;
import org.example.eventnotificator.notification.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRepository notificationRepository;

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void deleteOldNotifications() {
        log.info("trying to delete all notifications older than 7 days");
        LocalDateTime date = LocalDateTime.now().minusDays(7);
        List<NotificationEntity> oldNotifications = notificationRepository.findOldNotifications(date);
        if(!oldNotifications.isEmpty()) {
            notificationRepository.deleteAll(oldNotifications);
            log.info("count of deleted notifications: {}", oldNotifications.size());
        }
    }
}
