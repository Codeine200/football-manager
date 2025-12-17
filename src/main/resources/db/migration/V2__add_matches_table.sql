CREATE SEQUENCE football.matches_id_seq START WITH 1;

CREATE TABLE matches (
    id BIGINT PRIMARY KEY DEFAULT nextval('football.matches_id_seq'),
    season VARCHAR(20) NOT NULL,
    match_date DATE NOT NULL,
    is_finished BOOLEAN NOT NULL
);