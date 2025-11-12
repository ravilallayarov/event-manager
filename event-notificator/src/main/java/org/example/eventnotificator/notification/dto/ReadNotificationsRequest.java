package org.example.eventnotificator.notification.dto;

import java.util.List;

public record ReadNotificationsRequest(
        List<Long> notificationIds
) {
}
