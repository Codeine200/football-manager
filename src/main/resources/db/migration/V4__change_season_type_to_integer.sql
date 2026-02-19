ALTER TABLE football.matches
    ALTER COLUMN season TYPE INTEGER
        USING season::integer;