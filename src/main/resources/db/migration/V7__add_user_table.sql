CREATE SEQUENCE football.users_id_seq START WITH 1;

CREATE TABLE football.users (
    id BIGINT PRIMARY KEY DEFAULT nextval('football.users_id_seq'),
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

INSERT INTO football.users (username, password, role)
VALUES  ('admin', '$2a$10$0RZLKvQiuoLz3X3K44Ng3u8TwrGhU/uJdx7m9pAuS4afxqXYQf9H.', 'ADMIN'); -- password: password