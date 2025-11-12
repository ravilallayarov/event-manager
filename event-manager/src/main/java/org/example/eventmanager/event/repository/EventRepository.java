package org.example.eventmanager.event.repository;

import org.example.eventmanager.event.entity.EventEntity;
import org.example.eventmanager.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {
    List<EventEntity> getAllByUserEntity(UserEntity userEntity);
}
