ALTER TABLE football.match_stats
    ADD CONSTRAINT uq_match_stats_match_guest
        UNIQUE (match_id, is_guest);