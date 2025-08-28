package org.example.eventmanager.security.model;

public record User(
        Long id,
        String login,
        String role,
        Integer age
) {
}
