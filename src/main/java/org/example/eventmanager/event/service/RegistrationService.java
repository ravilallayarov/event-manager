package org.example.eventmanager.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.event.entity.EventStatus;
import org.example.eventmanager.event.entity.RegistrationEntity;
import org.example.eventmanager.event.mapper.EventMapper;
import org.example.eventmanager.event.model.Event;
import org.example.eventmanager.event.repository.RegistrationRepository;
import org.example.eventmanager.exception.EventRegistrationException;
import org.example.eventmanager.security.entity.UserEntity;
import org.example.eventmanager.security.model.User;
import org.example.eventmanager.security.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final EventService eventService;
    private final UserService userService;
    private final RegistrationRepository registrationRepository;
    private final EventMapper eventMapper;

    @Transactional
    public void registerUserForEvent(Long eventId) {
        UserEntity user = getCurrentUserEntity();
        log.info("trying to register user by id: {} for event by id: {}", user.getId(), eventId);

        EventEntity event = eventService.findEventEntityById(eventId);

        validateRegister(user, event);

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);

        RegistrationEntity registration = new RegistrationEntity(
                null,
                user,
                event,
                LocalDateTime.now());

        registrationRepository.save(registration);
        log.info("User with id: {} successfully registered for event {}", user.getId(), eventId);
    }

    @Transactional
    public void deleteRegister(Long eventId) {
        UserEntity user = getCurrentUserEntity();
        log.info("trying to cancelled register user by id: {} for event by id: {}", user.getId(), eventId);

        EventEntity event = eventService.findEventEntityById(eventId);

        validateDeleteRegister(user, event);

        RegistrationEntity register = registrationRepository.findByUserAndEvent(user, event);
        registrationRepository.delete(register);

        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
    }

    @Transactional(readOnly = true)
    public List<Event> getCurrentUserRegistrations() {
        UserEntity user = getCurrentUserEntity();
        List<EventEntity> events = registrationRepository.findAllEventByUser(user);
        return events.stream()
                .map(eventMapper::toModelFromEntity)
                .toList();
    }


    private void validateRegister(UserEntity user, EventEntity event) {
        boolean existRegister = registrationRepository.existsByUserAndEvent(user, event);
        if (existRegister) {
            throw new EventRegistrationException("User is already registered for this event");
        }

        boolean isCancelled = event.getStatus() == EventStatus.CANCELLED;
        boolean isFinished = event.getStatus() == EventStatus.FINISHED;
        if(isCancelled || isFinished) {
            throw new EventRegistrationException("Cannot register for this event");
        }

        boolean existAvailablePlaces = event.getOccupiedPlaces() < event.getMaxPlaces();
        if(!existAvailablePlaces) {
            throw new EventRegistrationException("No available places for this event");
        }
    }


    private void validateDeleteRegister(UserEntity user, EventEntity event) {
        boolean existRegister = registrationRepository.existsByUserAndEvent(user, event);
        if (!existRegister) {
            throw new EventRegistrationException("User is not registered for this event");
        }

        boolean isWaitStart = event.getStatus() == EventStatus.WAIT_START;
        if(!isWaitStart) {
            throw new EventRegistrationException("Cannot delete register for this event");
        }
    }


    private UserEntity getCurrentUserEntity() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findEntityById(currentUser.id());
    }
}
