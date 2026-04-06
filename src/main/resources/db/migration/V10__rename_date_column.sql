
ALTER TABLE IF EXISTS football.matches
    RENAME COLUMN match_date TO match_date_time;

ALTER TABLE IF EXISTS football.matches
    ALTER COLUMN match_date_time TYPE timestamp
        USING match_date_time::timestamp;

DROP INDEX IF EXISTS football.idx_matches_match_date;

CREATE INDEX IF NOT EXISTS idx_matches_match_date_time
    ON football.matches(match_date_time);