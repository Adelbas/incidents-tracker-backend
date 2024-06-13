package ru.adel.locationtracker.core.service.location.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "geospatial.property")
public record GeoSpatialProperty(int srid) { }
