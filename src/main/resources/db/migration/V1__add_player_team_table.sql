CREATE SEQUENCE football.team_id_seq START 1;
CREATE SEQUENCE football.player_id_seq START 1;

CREATE TABLE football.team (
    id BIGINT PRIMARY KEY DEFAULT nextval('football.team_id_seq'),
    name VARCHAR(100) NOT NULL
);

CREATE TABLE football.player (
    id BIGINT PRIMARY KEY DEFAULT nextval('football.player_id_seq'),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    id_team BIGINT REFERENCES football.team(id)
);

CREATE INDEX idx_player_id_team ON football.player(id_team);

-- Team 1: Red Dragons
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Red Dragons');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'John', 'Smith', 'Edward', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Mike', 'Johnson', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Alex', 'Williams', 'Paul', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Chris', 'Brown', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'David', 'Jones', 'Alan', currval('football.team_id_seq'));

-- Team 2: Blue Sharks
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Blue Sharks');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Peter', 'Lopez', 'James', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Ryan', 'Gonzalez', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Kevin', 'Wilson', 'Paul', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Brian', 'Anderson', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Jason', 'Thomas', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Eric', 'Taylor', NULL, currval('football.team_id_seq'));

-- Team 3: Golden Eagles
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Golden Eagles');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Luke', 'Perez', 'Alan', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Adam', 'Thompson', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Sean', 'White', 'James', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Aaron', 'Harris', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Nathan', 'Sanchez', 'Lee', currval('football.team_id_seq'));

-- Team 4: Silver Wolves
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Silver Wolves');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Dylan', 'Young', 'Paul', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Ethan', 'Allen', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Gabriel', 'King', 'Ray', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Isaac', 'Scott', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Christian', 'Green', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Nathan', 'Adams', NULL, currval('football.team_id_seq'));

-- Team 5: Black Panthers
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Black Panthers');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Kyle', 'Baker', 'Paul', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Brandon', 'Nelson', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Jordan', 'Carter', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Justin', 'Mitchell', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Aaron', 'Perez', 'Ray', currval('football.team_id_seq'));

-- Team 6: Green Giants
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Green Giants');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Samuel', 'Roberts', 'James', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Adam', 'Turner', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Connor', 'Phillips', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Evan', 'Campbell', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Nathan', 'Parker', 'Ray', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Derek', 'Evans', NULL, currval('football.team_id_seq'));

-- Team 7: White Tigers
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'White Tigers');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Jason', 'Edwards', 'Paul', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Eric', 'Collins', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Mark', 'Stewart', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Ryan', 'Simmons', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Brian', 'Perry', 'Ray', currval('football.team_id_seq'));

-- Team 8: Thunder Bulls
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Thunder Bulls');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Kevin', 'Cook', 'James', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Kyle', 'Morgan', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Adam', 'Bell', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Ethan', 'Murphy', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Luke', 'Bailey', 'Ray', currval('football.team_id_seq'));

-- Team 9: Fire Falcons
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Fire Falcons');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Christian', 'Rivera', 'Paul', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Dylan', 'Cooper', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Justin', 'Richardson', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Aaron', 'Cox', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Brandon', 'Howard', 'Ray', currval('football.team_id_seq'));

-- Team 10: Storm Hawks
INSERT INTO football.team (id, name)
VALUES (nextval('football.team_id_seq'), 'Storm Hawks');

INSERT INTO football.player (id, first_name, last_name, middle_name, id_team) VALUES
      (nextval('football.player_id_seq'), 'Nathan', 'Ward', 'James', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Samuel', 'Torres', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Evan', 'Peterson', 'Lee', currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Connor', 'Gray', NULL, currval('football.team_id_seq')),
      (nextval('football.player_id_seq'), 'Derek', 'Ramirez', 'Ray', currval('football.team_id_seq'));
