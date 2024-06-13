package ru.adel.locationtracker.core.service.location.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class GeoSpatialService {

    private final GeometryFactory geometryFactory;

    public GeoSpatialService(GeoSpatialProperty property) {
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), property.srid());
    }

    public Point getPoint(Double longitude, Double latitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public Integer getSrid() {
        return geometryFactory.getSRID();
    }
}
