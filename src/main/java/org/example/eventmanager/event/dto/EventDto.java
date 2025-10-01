package org.example.eventmanager.event.dto;

import java.time.LocalDateTime;

public record EventDto(
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
