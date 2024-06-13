--liquibase formatted sql

--changeset adel:create-table-user-location failOnError=true
--comment: Create table user_location
CREATE TABLE IF NOT EXISTS user_location
(
    user_id               UUID      DEFAULT gen_random_uuid(),
    latitude              DOUBLE PRECISION       NOT NULL,
    longitude             DOUBLE PRECISION       NOT NULL,
    coordinates           geography(POINT, 4326) NOT NULL,
    updated_at            TIMESTAMP DEFAULT clock_timestamp(),
    notification_distance INTEGER   DEFAULT 500,

    CONSTRAINT pk_user_location PRIMARY KEY (user_id),
    CONSTRAINT check_notification_distance CHECK (notification_distance BETWEEN 500 AND 30000)
);