package ru.adel.apigateway.public_interface.notification;

import ru.adel.apigateway.public_interface.notification.dto.NotificationMessage;

import java.util.UUID;

public interface NotificationService {

    void sendNotification(UUID userId, NotificationMessage notificationMessage);
}
