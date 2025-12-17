CREATE SEQUENCE football.match_stats_id_seq START WITH 1;

CREATE TABLE football.match_stats (
    id BIGINT PRIMARY KEY DEFAULT nextval('football.match_stats_id_seq'),
    match_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    is_guest BOOLEAN NOT NULL,
    score INT,
    is_winner BOOLEAN,
    goals INT
);
