CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    description  VARCHAR NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id     BIGINT REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date   TIMESTAMP WITHOUT TIME ZONE,
    end_date     TIMESTAMP WITHOUT TIME ZONE,
    item_id      BIGINT REFERENCES items (id),
    booker_id    BIGINT REFERENCES users (id),
    status       VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text      VARCHAR NOT NULL,
    item_id   BIGINT REFERENCES items (id),
    author_id BIGINT REFERENCES users (id),
    created   TIMESTAMP WITHOUT TIME ZONE
    );