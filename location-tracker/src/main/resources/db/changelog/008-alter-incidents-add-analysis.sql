--liquibase formatted sql

--changeset adel:008-incidents-add-description failOnError=true
--comment: Optional free-text description of the event (used together with title for categorization)
ALTER TABLE incidents
    ADD COLUMN IF NOT EXISTS description TEXT;

--changeset adel:008-incidents-add-category failOnError=true
--comment: Resolved category (nullable until enrichment completes)
ALTER TABLE incidents
    ADD COLUMN IF NOT EXISTS category_id BIGINT;

--changeset adel:008-incidents-add-danger-level failOnError=true
--comment: Resolved danger level
ALTER TABLE incidents
    ADD COLUMN IF NOT EXISTS danger_level VARCHAR(16);

--changeset adel:008-incidents-danger-check failOnError=true
ALTER TABLE incidents
    ADD CONSTRAINT check_incident_danger_level
        CHECK (danger_level IS NULL OR danger_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'));

--changeset adel:008-incidents-category-fk failOnError=true
--comment: FK from the partitioned incidents table to the non-partitioned category table (PostgreSQL 12+)
ALTER TABLE incidents
    ADD CONSTRAINT fk_incident_category
        FOREIGN KEY (category_id) REFERENCES category (id);
