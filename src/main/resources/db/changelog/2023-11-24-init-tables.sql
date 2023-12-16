--liquibase formatted sql

--changeset Kuznetsov.Mikhail:1

CREATE TABLE client_user
(
    id         UUID                     NOT NULL,
    login      VARCHAR                  NOT NULL
        CONSTRAINT uq_user_login UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT pk_client_user PRIMARY KEY (id)
);

CREATE TABLE note
(
    id         UUID                     NOT NULL,
    text       TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT pk_note PRIMARY KEY (id)
);

CREATE TABLE client_user_note
(
    id             UUID                     NOT NULL,
    client_user_id UUID                     NOT NULL REFERENCES client_user (id) ON DELETE CASCADE,
    note_id        UUID                     NOT NULL REFERENCES note (id) ON DELETE CASCADE,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT pk_user_note PRIMARY KEY (id)
);