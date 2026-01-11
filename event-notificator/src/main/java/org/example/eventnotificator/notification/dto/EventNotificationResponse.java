package org.example.eventnotificator.notification.dto;

import java.util.List;

public record EventNotificationResponse (
        Long eventId,
        List<ModifiedField> modifiedFields
){
}
