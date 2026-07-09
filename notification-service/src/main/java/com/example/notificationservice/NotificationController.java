package com.example.notificationservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @GetMapping("/notification")
    public String notification() {
        return "Notification Service Works!";
    }
}