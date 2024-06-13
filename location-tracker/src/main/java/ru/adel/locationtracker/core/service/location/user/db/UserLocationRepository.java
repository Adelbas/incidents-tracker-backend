package ru.adel.locationtracker.core.service.location.user.db;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.adel.locationtracker.core.service.location.user.db.entity.UserLocation;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, UUID> {

    @Query(
            value = "SELECT u.user_id " +
                    "FROM user_location u " +
                    "WHERE st_dwithin( " +
                    "               u.coordinates, " +
                    "               :point, " +
                    "               :maxDistance " +
                    ") AND st_dwithin( " +
                    "               u.coordinates, " +
                    "               :point, " +
                    "               u.notification_distance " +
                    ")",
            nativeQuery = true
    )
    List<UUID> findUsersToNotify(Point point, Integer maxDistance);
}
