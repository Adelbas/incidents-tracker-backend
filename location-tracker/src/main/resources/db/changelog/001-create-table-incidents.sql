--liquibase formatted sql

--changeset adel:create-table-incidents failOnError=true
--comment: Create table incidents and add partitions
CREATE TABLE IF NOT EXISTS incidents
(
    id              BIGSERIAL,
    posted_user_id  UUID                   NOT NULL,
    title           VARCHAR                NOT NULL,
    latitude        DOUBLE PRECISION       NOT NULL,
    longitude       DOUBLE PRECISION       NOT NULL,
    coordinates     geography(POINT, 4326) NOT NULL,
    created_at      TIMESTAMP DEFAULT clock_timestamp(),
    created_at_date DATE      DEFAULT clock_timestamp()::date,
    views           INTEGER   DEFAULT 0,

    CONSTRAINT pk_incidents PRIMARY KEY (id, created_at_date)
) PARTITION BY RANGE (created_at_date);

SELECT ${DB_PARTMAN_SCHEMA}.create_parent(
   p_parent_table := '${DB_SCHEMA}.incidents',
   p_control := 'created_at_date',
   p_type := 'native',
   p_interval := 'P1D',
   p_start_partition := '2024-01-01 00:00:00'::text,
   p_premake := '30'
);

UPDATE ${DB_PARTMAN_SCHEMA}.part_config
    SET infinite_time_partitions = true
    WHERE parent_table IN ('incidents');