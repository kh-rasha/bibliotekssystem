package com.example.bibliotekssystem.controller;

import com.example.bibliotekssystem.client.NotificationClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationTestController {

    private final NotificationClient notificationClient;

    public NotificationTestController(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @GetMapping("/test")
    public String testNotificationService() {
        return notificationClient.getNotification();
    }
}