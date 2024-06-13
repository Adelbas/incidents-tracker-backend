--liquibase formatted sql

--changeset adel:create-index-on-incidents failOnError=true
--comment: Create gist index on incidents
CREATE INDEX incidents_gist_index ON incidents USING gist (coordinates);