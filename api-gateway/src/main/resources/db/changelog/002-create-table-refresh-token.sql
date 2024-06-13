--liquibase formatted sql

--changeset adel:create-table-refresh-token failOnError=true
--comment: Create table refresh_token
CREATE TABLE IF NOT EXISTS refresh_token
(
    id         BIGSERIAL,
    user_id    UUID           NOT NULL,
    token      VARCHAR UNIQUE NOT NULL,
    issued_at  TIMESTAMP      NOT NULL,
    expired_at TIMESTAMP      NOT NULL,

    CONSTRAINT pk_refresh_token PRIMARY KEY (id),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id)
);