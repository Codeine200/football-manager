CREATE SEQUENCE football.match_stats_id_seq START WITH 1;

CREATE TABLE football.match_stats (
    id BIGINT PRIMARY KEY DEFAULT nextval('football.match_stats_id_seq'),
    match_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    team_type VARCHAR(10) NOT NULL,
    score INT NOT NULL,
    winner BOOLEAN NOT NULL,
    goals INT NOT NULL
);
