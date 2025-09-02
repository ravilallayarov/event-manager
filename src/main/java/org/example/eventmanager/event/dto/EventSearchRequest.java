package org.example.eventmanager.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.eventmanager.event.entity.EventStatus;

import java.time.LocalDateTime;

public record EventSearchRequest(
        String name,
        Integer placesMin,
        Integer placesMax,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dateStartAfter,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dateStartBefore,
        Double costMin,
        Double costMax,
        Integer durationMin,
        Integer durationMax,
        Integer locationId,
        EventStatus eventStatus
) {
}
