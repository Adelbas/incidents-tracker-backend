--liquibase formatted sql

--changeset adel:create-index-on-user-location failOnError=true
--comment: Create gist index on user_location
CREATE INDEX user_location_gist_index ON user_location USING gist (coordinates);