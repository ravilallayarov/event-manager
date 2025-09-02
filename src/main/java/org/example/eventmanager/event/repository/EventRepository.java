package org.example.eventmanager.event.repository;

import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {
    List<EventEntity> getAllByUserEntity(UserEntity userEntity);

    @Modifying
    @Query(value = "update events e set status = 'STARTED' where e.status = 'WAIT_START'" +
            "and e.date <= current_timestamp",
            nativeQuery = true)
    int updateStartedEvents();

    @Modifying
    @Query(value = "update events e set status = 'FINISHED' where e.status = 'STARTED'" +
            "and e.date + (e.duration || ' minutes')::interval <= current_timestamp",
            nativeQuery = true)
    int updateFinishedEvents();
}
