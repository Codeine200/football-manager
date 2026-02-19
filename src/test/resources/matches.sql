TRUNCATE TABLE football.team RESTART IDENTITY CASCADE;
TRUNCATE TABLE football.matches RESTART IDENTITY CASCADE;
TRUNCATE TABLE football.match_stats RESTART IDENTITY CASCADE;

INSERT INTO football.team (id, name) VALUES (1, 'Team A');
INSERT INTO football.team (id, name) VALUES (2, 'Team B');
INSERT INTO football.team (id, name) VALUES (3, 'Team C');
INSERT INTO football.team (id, name) VALUES (4, 'Team D');

INSERT INTO football.matches (id, season, match_date, is_finished)
VALUES (1, 2025, '2025-01-01', true);

INSERT INTO football.match_stats
(team_id, match_id, is_guest, score, is_winner, goals)
VALUES
    (1, 1, false, 3, true, 2),
    (2, 1, true, 0, false, 1);