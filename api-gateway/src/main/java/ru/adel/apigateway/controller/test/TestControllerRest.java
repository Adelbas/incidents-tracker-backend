package ru.adel.apigateway.controller.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.apigateway.public_interface.notification.NotificationService;
import ru.adel.apigateway.public_interface.notification.dto.NotificationMessage;

import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestControllerRest {

    private final NotificationService notificationService;

    @PostMapping("/notify/{userId}")
    public void createIncident(@PathVariable UUID userId, @RequestBody NotificationMessage notificationMessage) {
        notificationService.sendNotification(userId, notificationMessage);
    }
}
