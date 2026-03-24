CREATE INDEX IF NOT EXISTS idx_player_full_name_btree
    ON football.player (full_name);

CREATE INDEX IF NOT EXISTS idx_team_name_trgm
    ON football.team
        USING gin (LOWER(name) gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_team_name_btree
    ON team (name);