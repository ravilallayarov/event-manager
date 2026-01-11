package org.example.eventmanager.event.dto;

import java.util.List;

public record EventKafkaMessage(
        Long eventId,
        Long changedByUserId,
        Long ownerId,
        List<ModifiedField> modifiedFields,
        List<Long> subscribers
) {
    public record ModifiedField(
            String fieldName,
            String oldValue,
            String newValue
    ) {}
}
