package ru.adel.locationtracker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.adel.locationtracker.core.service.location.user.UserLocationService;
import ru.adel.locationtracker.public_interface.event.dto.NotificationDistanceUpdateRequest;

@Slf4j
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final UserLocationService userLocationService;

    @PutMapping("/notification")
    public void updateNotificationDistance(@RequestBody NotificationDistanceUpdateRequest request) {
        log.info("Handle update notification distance request: {}", request);
        userLocationService.updateNotificationDistance(request);
    }
}
