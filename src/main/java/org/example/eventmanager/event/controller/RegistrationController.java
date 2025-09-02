package org.example.eventmanager.event.controller;

import lombok.RequiredArgsConstructor;
import org.example.eventmanager.event.dto.EventDto;
import org.example.eventmanager.event.mapper.EventMapper;
import org.example.eventmanager.event.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final EventMapper eventMapper;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> registerUserForEvent(@PathVariable Long eventId) {
        registrationService.registerUserForEvent(eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/cancel/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteRegister(@PathVariable Long eventId) {
        registrationService.deleteRegister(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventDto>> getCurrentUserRegistrations() {
        List<EventDto> events = registrationService.getCurrentUserRegistrations().stream()
                .map(eventMapper::toDtoFromModel)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }
}
