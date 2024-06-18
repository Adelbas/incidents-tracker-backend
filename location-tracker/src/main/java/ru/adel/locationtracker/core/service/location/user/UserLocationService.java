package ru.adel.locationtracker.core.service.location.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.location.user.db.UserLocationDbService;
import ru.adel.locationtracker.core.service.location.user.db.entity.UserLocation;
import ru.adel.locationtracker.public_interface.event.dto.NotificationDistanceUpdateRequest;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationDto;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationMessage;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserLocationService {

    private final UserLocationDbService userLocationDbService;

    public UserLocationDto saveLocation(UserLocationMessage userLocationMessage) {
        UserLocation userLocation = userLocationDbService.saveOrUpdate(userLocationMessage);
        return mapToDto(userLocation);
    }

    public List<UUID> getUsersToNotifyForCoordinates(Double longitude, Double latitude) {
        return userLocationDbService.findUsersToNotifyForCoordinates(longitude, latitude);
    }

    public void updateNotificationDistance(NotificationDistanceUpdateRequest notificationDistanceUpdateRequest) {
        UserLocation user = userLocationDbService.getUserById(notificationDistanceUpdateRequest.userId());
        user.setNotificationDistance(notificationDistanceUpdateRequest.distance());
        userLocationDbService.save(user);
    }

    private UserLocationDto mapToDto(UserLocation userLocation) {
        return UserLocationDto.builder()
                .userId(userLocation.getUserId())
                .longitude(userLocation.getLongitude())
                .latitude(userLocation.getLatitude())
                .notificationDistance(userLocation.getNotificationDistance())
                .build();
    }
}
