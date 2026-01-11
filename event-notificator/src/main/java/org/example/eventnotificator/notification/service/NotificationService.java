package org.example.eventnotificator.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventnotificator.notification.dto.EventKafkaMessage;
import org.example.eventnotificator.notification.dto.EventNotificationResponse;
import org.example.eventnotificator.notification.dto.ModifiedField;
import org.example.eventnotificator.notification.entity.ModifiedFieldEntity;
import org.example.eventnotificator.notification.entity.NotificationEntity;
import org.example.eventnotificator.notification.entity.UserNotification;
import org.example.eventnotificator.notification.repository.NotificationRepository;
import org.example.eventnotificator.security.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<EventNotificationResponse> findAll() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("trying to find all notification for userId:{}", currentUser.id());

        List<NotificationEntity> notifications = notificationRepository.findAllNotificationByUserId(currentUser.id());
        List<EventNotificationResponse> response = new ArrayList<>();

        for (NotificationEntity notification : notifications) {
            EventNotificationResponse notificationResponse = new EventNotificationResponse(
                    notification.getEventId(),
                    notification.getModifiedFields().stream()
                            .map((entity) -> new ModifiedField(entity.getFieldName(),
                                    entity.getOldValue(), entity.getNewValue())).toList());
            response.add(notificationResponse);
        }

        return response;
    }

    @Transactional
    public void readNotifications(List<Long> notificationIds) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("trying to read all notifications with ids: {}", notificationIds);
        notificationRepository.readNotifications(notificationIds, currentUser.id());
    }


    @Transactional
    public void saveEventNotification(EventKafkaMessage message) {
        log.info("trying to save an event notification: {}", message);

        NotificationEntity notification = new NotificationEntity(null, message.eventId(), message.changedByUserId(),
                message.ownerId(), null, null, null);

        List<UserNotification> userNotifications = new ArrayList<>();
        for (Long userId : message.subscribers()) {
            UserNotification userNotification = new UserNotification();
            userNotification.setUserId(userId);
            userNotification.setNotificationEntity(notification);
            userNotifications.add(userNotification);
        }

        List<ModifiedFieldEntity> modifiedFields = message.modifiedFields().stream()
                .map(modifiedField -> new ModifiedFieldEntity(null, modifiedField.fieldName(),
                        modifiedField.oldValue(), modifiedField.newValue(), notification))
                .toList();

        notification.setUserNotifications(userNotifications);
        notification.setModifiedFields(modifiedFields);

        notificationRepository.save(notification);
        log.info("an event notification successfully saved: {}", message);
    }

}
