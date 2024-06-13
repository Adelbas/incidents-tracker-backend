package ru.adel.locationtracker.core.service.location.user.db;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.adel.locationtracker.core.service.location.utils.GeoSpatialService;
import ru.adel.locationtracker.core.service.location.user.db.entity.UserLocation;
import ru.adel.locationtracker.public_interface.event.dto.UserLocationMessage;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserLocationDbService {

    @Value("${geospatial.property.min-notification-distance}")
    private Integer minNotificationDistance;

    @Value("${geospatial.property.max-notification-distance}")
    private Integer maxNotificationDistance;

    private final UserLocationRepository userLocationRepository;

    private final GeoSpatialService geoSpatialService;

    public UserLocation saveOrUpdate(UserLocationMessage userLocationMessage) {
        UserLocation userLocation = userLocationRepository.findById(userLocationMessage.userId())
                .map(existingLocation -> updateLocationIfNecessary(existingLocation, userLocationMessage))
                .orElseGet(() -> createNewLocation(userLocationMessage));

        return userLocationRepository.save(userLocation);
    }


    public List<UUID> findUsersToNotifyForCoordinates(Double longitude, Double latitude) {
        Point point = geoSpatialService.getPoint(longitude, latitude);
        return userLocationRepository.findUsersToNotify(point, maxNotificationDistance);
    }

    private UserLocation createNewLocation(UserLocationMessage userLocationMessage) {
        return UserLocation.builder()
                .userId(userLocationMessage.userId())
                .latitude(userLocationMessage.latitude())
                .longitude(userLocationMessage.longitude())
                .coordinates(
                        geoSpatialService.getPoint(
                                userLocationMessage.longitude(),
                                userLocationMessage.latitude()
                        )
                )
                .notificationDistance(minNotificationDistance)
                .updatedAt(userLocationMessage.timestamp())
                .build();
    }

    private UserLocation updateLocationIfNecessary(UserLocation existingLocation, UserLocationMessage userLocationMessage) {
        if (existingLocation.getUpdatedAt().isBefore(userLocationMessage.timestamp())) {
            existingLocation.setLatitude(userLocationMessage.latitude());
            existingLocation.setLongitude(userLocationMessage.longitude());
            existingLocation.setCoordinates(
                    geoSpatialService.getPoint(
                            userLocationMessage.longitude(),
                            userLocationMessage.latitude()
                    )
            );
            existingLocation.setUpdatedAt(userLocationMessage.timestamp());
        }
        return existingLocation;
    }
}
