--liquibase formatted sql

--changeset adel:006-create-table-category failOnError=true
--comment: Create category table for dynamic, admin-managed event categorization
CREATE TABLE IF NOT EXISTS category
(
    id                BIGSERIAL,
    code              VARCHAR(64)  NOT NULL,
    name              VARCHAR(255) NOT NULL,
    description       TEXT         NOT NULL,
    base_danger_level VARCHAR(16)  NOT NULL,
    active            BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at        TIMESTAMP    NOT NULL DEFAULT clock_timestamp(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT clock_timestamp(),

    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_code UNIQUE (code),
    CONSTRAINT check_category_base_danger CHECK (base_danger_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'))
);
