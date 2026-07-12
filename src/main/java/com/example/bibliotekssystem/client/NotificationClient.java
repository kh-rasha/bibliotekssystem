package com.example.bibliotekssystem.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @GetMapping("/notification")
    String getNotification();
}