package org.example.eventmanager.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateOrUpdateEventRequest(
        @NotBlank(message = "event name cannot be blank")
        String name,

        @NotNull(message = "event max places cannot be null")
        @Min(value = 2, message = "event max places must be at least 2")
        Integer maxPlaces,

        @NotNull(message = "event date cannot be null")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @FutureOrPresent(message = "event date must be in the present or future")
        LocalDateTime date,

        @NotNull
        @Min(value = 0, message = "event cost must be at least 0 rub")
        Integer cost,

        @NotNull
        @Min(value = 30, message = "event duration must be at least 30 min")
        Integer duration,

        @NotNull(message = "locationId cannot be null")
        Long locationId) {
}
