package ru.adel.apigateway.core.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.public_interface.notification.NotificationService;
import ru.adel.apigateway.public_interface.notification.dto.NotificationMessage;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(UUID userId, NotificationMessage notificationMessage) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/private", notificationMessage
        );
    }
}