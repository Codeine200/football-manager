# 🏟 Football Manager Project – HELP.md

## 📖 Overview
This is a **Football Manager** project for managing **teams, players and matches**.  
It provides **REST APIs** for CRUD operations, supports **photo uploads**, and includes automated **tests** for core functionality.

The project uses:

- **Spring Boot** – backend framework
- **PostgreSQL** – database
- **Docker & Docker Compose** – for easy deployment

---

## ✨ Features

### Teams
- Create, read, update, delete teams.
- Upload a **team logo** via REST API.
- Team data stored in PostgreSQL.

### Players
- Manage player details (first name, last name, middle name).
- Assign players to teams.
- Upload **player photos** via REST API.
- Player data stored in PostgreSQL.
-
### Matches
- Create, read, update, delete matches.

### REST API
- Fully exposed CRUD operations.
- Supports `multipart/form-data` for photo uploads.
- Configurable with **environment variables**:
    - `SPRING_PROFILES_ACTIVE`
    - `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`
    - `UPLOADS_TEAMS_PHOTOS_FOLDER`, `UPLOADS_PLAYERS_PHOTOS_FOLDER`

### File Uploads
- Photos stored in a host folder mapped via Docker.
- Configurable **upload paths** using `.env`


## ⚽ Matches

This section manages **football matches** between teams.

### Features
- Create, read, update, and delete matches.
- Returns **detailed team statistics** in the response, including:
    - `teamId` – ID of the team
    - `teamName` – name of the team
    - `isGuest` – whether the team is guest
    - `goals` – number of goals scored
    - `score` – points for the match
    - `isWinner` – indicates if the team won the match

### REST API Endpoints

| Method | Endpoint               | Description                        |
|--------|-----------------------|------------------------------------|
| POST   | `/api/v1/matches`     | Create a new match                 |
| GET    | `/api/v1/matches`     | List all matches                   |
| GET    | `/api/v1/matches/{id}`| Get match details by ID            |
| PUT    | `/api/v1/matches/{id}`| Update match information           |
| DELETE | `/api/v1/matches/{id}`| Delete a match                     |

### Example JSON Payload

```json
{
  "season": 2026,
  "matchDate": "2026-03-01",
  "team1": {
    "id": 1,
    "isGuest": false
  },
  "team2": {
    "id": 2,
    "isGuest": true
  }
}
