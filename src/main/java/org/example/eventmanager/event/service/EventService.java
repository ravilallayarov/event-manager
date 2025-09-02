package org.example.eventmanager.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.event.dto.CreateOrUpdateEventRequest;
import org.example.eventmanager.event.dto.EventSearchRequest;
import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.event.entity.EventStatus;
import org.example.eventmanager.event.mapper.EventMapper;
import org.example.eventmanager.event.model.Event;
import org.example.eventmanager.event.repository.EventRepository;
import org.example.eventmanager.event.util.EventSpecification;
import org.example.eventmanager.exception.EventChangedException;
import org.example.eventmanager.exception.InvalidCapacityException;
import org.example.eventmanager.exception.NotFoundException;
import org.example.eventmanager.location.entity.LocationEntity;
import org.example.eventmanager.location.service.LocationService;
import org.example.eventmanager.security.entity.Role;
import org.example.eventmanager.security.entity.UserEntity;
import org.example.eventmanager.security.model.User;
import org.example.eventmanager.security.service.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final EventMapper eventMapper;
    private final UserService userService;

    @Transactional
    public Event createEvent(CreateOrUpdateEventRequest request) {
        log.info("trying to create event with name: {}", request.name());

        LocationEntity location = locationService.findEntityById(request.locationId());
        boolean isOverCapacity = request.maxPlaces() > location.getCapacity();
        if(isOverCapacity) {
            throw new InvalidCapacityException("The maximum number of seats exceeds the capacity of the location");
        }

        UserEntity user = getCurrentUserEntity();

        EventEntity eventToSave = eventMapper.toEntityFromRequest(request);

        eventToSave.setUserEntity(user);
        eventToSave.setLocationEntity(location);
        EventEntity savedEvent = eventRepository.save(eventToSave);
        log.info("event with name: {}, successfully saved", request.name());
        return eventMapper.toModelFromEntity(savedEvent);
    }

    @Transactional
    public void deleteEvent(Long eventId) throws AccessDeniedException {
        log.info("trying to delete event with id: {}", eventId);

        EventEntity event = findEventEntityById(eventId);
        UserEntity currentUser = getCurrentUserEntity();

        checkAccess(currentUser, event);

        if(event.getStatus() == EventStatus.CANCELLED) {
            throw new EventChangedException("The event has already been deleted");
        }

        if(!(event.getStatus() == EventStatus.WAIT_START)) {
            throw new EventChangedException("The event cannot be deleted once started");
        }


        event.setStatus(EventStatus.CANCELLED);
        log.info("event with id: {} successfully deleted", eventId);
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public EventEntity findEventEntityById(Long eventId) {
        log.info("trying to find event with id: {}", eventId);
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id: " + eventId + " not found"));
    }

    @Transactional(readOnly = true)
    public Event findEventById(Long eventId) {
        return eventMapper.toModelFromEntity(findEventEntityById(eventId));
    }

    @Transactional
    public Event updateEvent(Long eventId, CreateOrUpdateEventRequest request) throws AccessDeniedException {
        log.info("trying to update event with id: {}", eventId);

        EventEntity event = findEventEntityById(eventId);
        UserEntity currentUser = getCurrentUserEntity();

        checkAccess(currentUser, event);

        if(!(event.getStatus() == EventStatus.WAIT_START)) {
            throw new EventChangedException("The event cannot be changed once started or cancelled.");
        }

        LocationEntity location = locationService.findEntityById(request.locationId());
        boolean isOverCapacity = request.maxPlaces() > location.getCapacity();
        boolean isLessThanOccupiedSeats = request.maxPlaces() < event.getOccupiedPlaces();
        if(isOverCapacity || isLessThanOccupiedSeats) {
            throw new InvalidCapacityException("Invalid maximum seats value specified");
        }

        eventMapper.updateEventFromRequest(event, request);
        event.setLocationEntity(location);

        eventRepository.save(event);
        log.info("event with id: {}, successfully updated", eventId);

        return eventMapper.toModelFromEntity(event);
    }

    @Transactional(readOnly = true)
    public List<Event> searchEvents(EventSearchRequest request) {
        log.info("trying to get all events with filtering");
        Specification<EventEntity> spec = EventSpecification.build(request);
        return eventRepository.findAll(spec).stream()
                .map(eventMapper::toModelFromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Event> getUserEvents() {
        UserEntity user = getCurrentUserEntity();
        List<EventEntity> events = eventRepository.getAllByUserEntity(user);
        return events.stream()
                .map(eventMapper::toModelFromEntity)
                .toList();
    }

    private UserEntity getCurrentUserEntity() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findEntityById(currentUser.id());
    }

    private void checkAccess(UserEntity currentUser, EventEntity event) throws AccessDeniedException {
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = currentUser.getId().equals(event.getUserEntity().getId());

        if(!isAdmin && !isOwner) {
            throw new AccessDeniedException("You are not allowed to modify this event");
        }
    }
}
