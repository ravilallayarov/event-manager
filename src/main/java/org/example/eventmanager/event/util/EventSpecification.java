package org.example.eventmanager.event.util;

import org.example.eventmanager.event.dto.EventSearchRequest;
import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.event.entity.EventStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class EventSpecification {

    public static Specification<EventEntity> build(EventSearchRequest request) {
        return Specification.allOf(hasName(request.name()))
                .and(hasPlacesMin(request.placesMin()))
                .and(hasPlacesMax(request.placesMax()))
                .and(hasDateStartAfter(request.dateStartAfter()))
                .and(hasDateStartBefore(request.dateStartBefore()))
                .and(hasCostMin(request.costMin()))
                .and(hasCostMax(request.costMax()))
                .and(hasDurationMin(request.durationMin()))
                .and(hasDurationMax(request.durationMax()))
                .and(hasLocationId(request.locationId()))
                .and(hasEventStatus(request.eventStatus()));
    }

    private static Specification<EventEntity> hasName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<EventEntity> hasPlacesMin(Integer min) {
        return (root, query, cb) -> {
            if (min == null) {
                return cb.conjunction();
            }
            return cb.ge(root.get("maxPlaces"), min);
        };
    }

    private static Specification<EventEntity> hasPlacesMax(Integer max) {
        return (root, query, cb) -> {
            if (max == null) {
                return cb.conjunction();
            }
            return cb.le(root.get("maxPlaces"), max);
        };
    }

    private static Specification<EventEntity> hasDateStartAfter(LocalDateTime dateStartAfter) {
        return (root, query, cb) -> {
            if (dateStartAfter == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("date"), dateStartAfter);
        };
    }

    private static Specification<EventEntity> hasDateStartBefore(LocalDateTime dateStartBefore) {
        return (root, query, cb) -> {
            if (dateStartBefore == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("date"), dateStartBefore);
        };
    }

    private static Specification<EventEntity> hasCostMin(Double min) {
        return (root, query, cb) -> {
            if (min == null) {
                return cb.conjunction();
            }
            return cb.ge(root.get("cost"), min);
        };
    }

    private static Specification<EventEntity> hasCostMax(Double max) {
        return (root, query, cb) -> {
            if (max == null) {
                return cb.conjunction();
            }
            return cb.le(root.get("cost"), max);
        };
    }

    private static Specification<EventEntity> hasDurationMin(Integer min) {
        return (root, query, cb) -> {
            if (min == null) {
                return cb.conjunction();
            }
            return cb.ge(root.get("duration"), min);
        };
    }

    private static Specification<EventEntity> hasDurationMax(Integer max) {
        return (root, query, cb) -> {
            if (max == null) {
                return cb.conjunction();
            }
            return cb.le(root.get("duration"), max);
        };
    }

    private static Specification<EventEntity> hasLocationId(Integer locationId) {
        return (root, query, cb) -> {
            if (locationId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("locationEntity").get("id"), locationId);
        };
    }

    private static Specification<EventEntity> hasEventStatus(EventStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }
}

