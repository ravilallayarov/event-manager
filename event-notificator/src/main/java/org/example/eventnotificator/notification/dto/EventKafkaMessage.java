package org.example.eventnotificator.notification.dto;

import java.util.List;

public record EventKafkaMessage(
        Long eventId,
        Long changedByUserId,
        Long ownerId,
        List<ModifiedField> modifiedFields,
        List<Long> subscribers
) {}
