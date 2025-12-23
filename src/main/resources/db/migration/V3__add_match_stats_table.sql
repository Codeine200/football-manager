CREATE SEQUENCE football.match_stats_id_seq START WITH 1;

CREATE TABLE football.match_stats (
    id BIGINT PRIMARY KEY DEFAULT nextval('football.match_stats_id_seq'),
    match_id BIGINT NOT NULL,
    team_id  BIGINT NOT NULL,
    is_guest BOOLEAN NOT NULL,
    score INT,
    is_winner BOOLEAN,
    goals INT,

    CONSTRAINT fk_match_stats_match
      FOREIGN KEY (match_id)
          REFERENCES football.matches(id),

    CONSTRAINT fk_match_stats_team
      FOREIGN KEY (team_id)
          REFERENCES football.team(id),

    CONSTRAINT uq_match_team
      UNIQUE (match_id, team_id),

    CONSTRAINT chk_goals_non_negative
      CHECK (goals IS NULL OR goals >= 0),

    CONSTRAINT chk_score_non_negative
      CHECK (score IS NULL OR score >= 0)
);

CREATE INDEX idx_match_stats_match_id
    ON football.match_stats (match_id);

CREATE INDEX idx_match_stats_team_id
    ON football.match_stats (team_id);
