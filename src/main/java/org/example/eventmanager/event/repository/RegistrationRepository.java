package org.example.eventmanager.event.repository;

import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.event.entity.RegistrationEntity;
import org.example.eventmanager.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    boolean existsByUserAndEvent(UserEntity user, EventEntity event);

    RegistrationEntity findByUserAndEvent(UserEntity user, EventEntity event);

    @Query("select r.event from RegistrationEntity r where r.user = :user")
    List<EventEntity> findAllEventByUser(@Param("user") UserEntity user);
}
