--liquibase formatted sql

--changeset adel:create-table-users failOnError=true
--comment: Create table users
CREATE TABLE IF NOT EXISTS users
(
    id         UUID DEFAULT gen_random_uuid(),
    email      VARCHAR UNIQUE NOT NULL,
    first_name VARCHAR        NOT NULL,
    last_name  VARCHAR        NOT NULL,
    password   VARCHAR        NOT NULL,
    role       VARCHAR        NOT NULL,
    active     BOOLEAN        NOT NULL,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT check_role CHECK (role IN ('USER', 'ADMIN'))
);