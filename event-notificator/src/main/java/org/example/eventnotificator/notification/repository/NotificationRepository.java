package org.example.eventnotificator.notification.repository;

import org.example.eventnotificator.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query(value = "select u.notificationEntity from UserNotification u where u.userId = :userId and u.isRead = false")
    List<NotificationEntity> findAllNotificationByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "update UserNotification u set u.isRead = true where u.id in :notificationIds and u.userId = :userId")
    void readNotifications(@Param("notificationIds") List<Long> notificationIds,
                           @Param("userId") Long userId);

    @Query(value = "select n from NotificationEntity n where n.createdAt < :date")
    List<NotificationEntity> findOldNotifications(@Param("date") LocalDateTime date);
}
