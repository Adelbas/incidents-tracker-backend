--liquibase formatted sql

--changeset adel:create-table-user-incident-interaction failOnError=true
--comment: Create table user_incident_interaction and add partitions
CREATE TABLE IF NOT EXISTS incident_user_interaction
(
    incident_id   BIGINT  NOT NULL,
    incident_date DATE    NOT NULL,
    user_id       UUID    NOT NULL,
    status        VARCHAR NOT NULL,
    updated_at    TIMESTAMP DEFAULT clock_timestamp(),

    CONSTRAINT pk_user_incident_interaction PRIMARY KEY (incident_id, user_id, incident_date),
    CONSTRAINT fk_user_incident_interaction_incident FOREIGN KEY (incident_id, incident_date) REFERENCES incidents (id, created_at_date),
    CONSTRAINT check_status CHECK (status IN ('NOTIFIED', 'VIEWED'))
) partition by RANGE (incident_date);

SELECT ${DB_PARTMAN_SCHEMA}.create_parent(
               p_parent_table := '${DB_SCHEMA}.incident_user_interaction',
               p_control := 'incident_date',
               p_type := 'native',
               p_interval := 'P1D',
               p_start_partition := '2024-01-01 00:00:00'::text,
               p_premake := '30'
);

UPDATE ${DB_PARTMAN_SCHEMA}.part_config
    SET infinite_time_partitions = true
WHERE parent_table IN ('incident_user_interaction');