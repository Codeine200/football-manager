ALTER TABLE football.player
    DROP COLUMN IF EXISTS first_name,
    DROP COLUMN IF EXISTS last_name,
    DROP COLUMN IF EXISTS middle_name;

TRUNCATE TABLE football.team RESTART IDENTITY CASCADE;
TRUNCATE TABLE football.player RESTART IDENTITY CASCADE;

ALTER TABLE football.player
    ADD COLUMN IF NOT EXISTS full_name TEXT NOT NULL;

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_player_full_name_trgm
    ON football.player USING gin (LOWER(full_name) gin_trgm_ops);

INSERT INTO football.team (id, name) VALUES
    (1, 'Chelsea'),
    (2, 'Barcelona');

INSERT INTO football.player (id, full_name, id_team, photo) VALUES
   (1, 'Joan García Pons', 2, '1.webp'),
   (2, 'Wojciech Szczęsny', 2, '2.webp'),
   (3, 'Eder Aller González', 2, '3.webp'),
   (4, 'Diego Kochen', 2, '4.webp'),
   (5, 'Andreas Christensen', 2, '5.webp'),
   (6, 'Xavi Espart Font', 2, '6.webp'),
   (7, 'João Pedro Cavaco Cancelo', 2, '7.webp'),
   (8, 'Jofre Torrents Salvat', 2, '8.webp'),
   (9, 'Koundé Jules Olivier', 2, '9.webp'),
   (10, 'Szczęsny Wojciech', 2, '10.webp'),
   (11, 'Carvajal Daniel Olmo Dani', 2, '11.webp'),
   (12, 'González Eder Aller', 2, '12.webp');