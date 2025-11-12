package org.example.eventmanager.event.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.eventmanager.event.entity.EventEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomEventRepository {

    private final EntityManager entityManager;

    @Transactional
    @SuppressWarnings("unchecked")
    public List<EventEntity> updateStartedEvents() {
        return entityManager.createNativeQuery("UPDATE events e SET status = 'STARTED'" +
                "WHERE e.status = 'WAIT_START' AND e.date <= current_timestamp RETURNING *",
                EventEntity.class).getResultList();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<EventEntity> updateFinishedEvents() {
        return entityManager.createNativeQuery("UPDATE events e SET status = 'FINISHED'" +
                        "WHERE e.status = 'STARTED' and " +
                        "e.date + (e.duration || ' minutes')::interval <= current_timestamp RETURNING *",
                EventEntity.class).getResultList();
    }
}
