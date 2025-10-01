package org.example.eventmanager.event.mapper;

import org.example.eventmanager.event.dto.CreateOrUpdateEventRequest;
import org.example.eventmanager.event.dto.EventDto;
import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.event.entity.EventStatus;
import org.example.eventmanager.event.model.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public EventEntity toEntityFromRequest(CreateOrUpdateEventRequest request) {
        if ( request == null ) {
            return null;
        }

        EventEntity eventEntity = new EventEntity();

        eventEntity.setName( request.name() );
        eventEntity.setMaxPlaces( request.maxPlaces() );
        eventEntity.setDate( request.date() );
        eventEntity.setCost( request.cost() );
        eventEntity.setDuration( request.duration() );
        eventEntity.setStatus(EventStatus.WAIT_START);
        eventEntity.setOccupiedPlaces(0);

        return eventEntity;
    }

    public Event toModelFromEntity(EventEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Integer maxPlaces = null;
        LocalDateTime date = null;
        Integer cost = null;
        Integer duration = null;
        Integer occupiedPlaces = null;
        String status = null;
        Long locationId = null;
        Long ownerId = null;

        id = entity.getId();
        name = entity.getName();
        maxPlaces = entity.getMaxPlaces();
        date = entity.getDate();
        cost = entity.getCost();
        duration = entity.getDuration();
        occupiedPlaces = entity.getOccupiedPlaces();
        if ( entity.getStatus() != null ) {
            status = entity.getStatus().name();
        }
        locationId = entity.getLocationEntity().getId();
        ownerId = entity.getUserEntity().getId();

        Event event = new Event( id, name, maxPlaces, date, cost, duration, locationId, ownerId, occupiedPlaces, status );

        return event;
    }

    public EventDto toDtoFromModel(Event event) {
        if ( event == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Integer maxPlaces = null;
        LocalDateTime date = null;
        Integer cost = null;
        Integer duration = null;
        Long locationId = null;
        Long ownerId = null;
        Integer occupiedPlaces = null;
        String status = null;

        id = event.id();
        name = event.name();
        maxPlaces = event.maxPlaces();
        date = event.date();
        cost = event.cost();
        duration = event.duration();
        locationId = event.locationId();
        ownerId = event.ownerId();
        occupiedPlaces = event.occupiedPlaces();
        status = event.status();

        EventDto eventDto = new EventDto( id, name, maxPlaces, date, cost, duration, locationId, ownerId, occupiedPlaces, status );

        return eventDto;
    }

    public void updateEventFromRequest(EventEntity event, CreateOrUpdateEventRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.date() != null) {
            event.setDate(request.date());
        }
        if (request.duration() != null) {
            event.setDuration(request.duration());
        }
        if (request.name() != null) {
            event.setName(request.name());
        }
        if (request.cost() != null) {
            event.setCost(request.cost());
        }
        if (request.maxPlaces() != null) {
            event.setMaxPlaces(request.maxPlaces());
        }
    }
}
