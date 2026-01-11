package org.example.eventnotificator.notification.dto;

public record ModifiedField(
        String fieldName,
        String oldValue,
        String newValue
) {}
