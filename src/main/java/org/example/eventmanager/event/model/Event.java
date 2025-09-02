package org.example.eventmanager.event.model;

import java.time.LocalDateTime;

public record Event(
        Long id,
        String name,
        Integer maxPlaces,
        LocalDateTime date,
        Integer cost,
        Integer duration,
        Long locationId,
        Long ownerId,
        Integer occupiedPlaces,
        String status
) {
}
