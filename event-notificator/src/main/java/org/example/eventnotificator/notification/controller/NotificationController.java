package org.example.eventnotificator.notification.controller;

import lombok.RequiredArgsConstructor;
import org.example.eventnotificator.notification.dto.EventNotificationResponse;
import org.example.eventnotificator.notification.dto.ReadNotificationsRequest;
import org.example.eventnotificator.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<EventNotificationResponse>> findAll() {
        List<EventNotificationResponse> notifications = notificationService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }

    @PostMapping
    public ResponseEntity<Void> readNotifications(@RequestBody ReadNotificationsRequest request) {
        notificationService.readNotifications(request.notificationIds());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
