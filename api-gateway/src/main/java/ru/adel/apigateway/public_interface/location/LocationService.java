package ru.adel.apigateway.public_interface.location;

import ru.adel.apigateway.public_interface.location.dto.LocationMessage;

import java.util.UUID;

public interface LocationService {

    void saveLocation(UUID userId, LocationMessage locationMessage);
}
