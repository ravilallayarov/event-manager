package org.example.eventmanager.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventmanager.event.dto.CreateOrUpdateEventRequest;
import org.example.eventmanager.event.dto.EventDto;
import org.example.eventmanager.event.dto.EventSearchRequest;
import org.example.eventmanager.event.mapper.EventMapper;
import org.example.eventmanager.event.model.Event;
import org.example.eventmanager.event.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody CreateOrUpdateEventRequest request) {
        Event event = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.toDtoFromModel(event));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) throws AccessDeniedException {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        Event event = eventService.findEventById(id);
        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.toDtoFromModel(event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EventDto> updateEventById(@PathVariable Long id,
                                                    @Valid @RequestBody CreateOrUpdateEventRequest request)
            throws AccessDeniedException {
        Event event = eventService.updateEvent(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.toDtoFromModel(event));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<EventDto>> searchEvents(@RequestBody EventSearchRequest request) {
        List<EventDto> events = eventService.searchEvents(request).stream()
                .map(eventMapper::toDtoFromModel)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventDto>> getUserEvents() {
        List<EventDto> userEvents = eventService.getUserEvents().stream()
                .map(eventMapper::toDtoFromModel)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userEvents);

    }
}
