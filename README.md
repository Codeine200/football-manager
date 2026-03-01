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

## Team API Endpoints

| HTTP Method | URL | Description | Request Body | Response Code | Example Response |
|------------|-----|------------|--------------|---------------|-----------------|
| GET | `/api/v1/teams` | Get all teams with pagination | — | 200 OK | `{ "content": [...], "totalElements": 10, "totalPages": 1 }` |
| GET | `/api/v1/teams/{id}` | Get team by ID | — | 200 OK | `{ "id": 1, "name": "Barcelona", "logoUrl": "/files/1.png" }` |
| POST | `/api/v1/teams` | Create a new team with optional logo | `TeamRequestDto` (multipart/form-data) + optional `file` | 201 Created | `{ "id": 1, "name": "Barcelona", "logoUrl": "/files/1.png" }` |
| PUT | `/api/v1/teams/{id}` | Update team info and optionally change logo | `TeamRequestDto` (multipart/form-data) + optional `file` | 200 OK | `{ "id": 1, "name": "Barcelona", "logoUrl": "/files/1_updated.png" }` |
| DELETE | `/api/v1/teams/{id}` | Delete a team | — | 204 No Content | — |

### Players
- Manage player details (first name, last name, middle name).
- Assign players to teams.
- Upload **player photos** via REST API.
- Player data stored in PostgreSQL.

## Player API Endpoints

| HTTP Method | URL | Description | Request Body | Response Code | Example Response |
|------------|-----|------------|--------------|---------------|-----------------|
| GET | `/api/v1/players` | Get all players, optionally filtered by teamId, with pagination | `teamId` (query param, optional) | 200 OK | `{ "content": [...], "totalElements": 20, "totalPages": 2 }` |
| GET | `/api/v1/players/{id}` | Get player by ID | — | 200 OK | `{ "id": 1, "firstName": "Lionel", "lastName": "Messi", "teamId": 1 }` |
| POST | `/api/v1/players` | Create a new player with optional photo | `PlayerRequestDto` (multipart/form-data) + optional `file` | 201 Created | `{ "id": 1, "firstName": "Lionel", "lastName": "Messi", "teamId": 1, "photoUrl": "/files/1.jpg" }` |
| PUT | `/api/v1/players/{id}` | Update player info and optionally change photo | `PlayerRequestDto` (multipart/form-data) + optional `file` | 200 OK | `{ "id": 1, "firstName": "Lionel", "lastName": "Messi", "teamId": 1, "photoUrl": "/files/1_updated.jpg" }` |
| PUT | `/api/v1/players/{id}/assign` | Assign player to a team or change team | `PlayerAssignRequestDto` | 200 OK | `{ "id": 1, "firstName": "Lionel", "lastName": "Messi", "teamId": 2 }` |
| DELETE | `/api/v1/players/{id}` | Delete a player | — | 204 No Content | — |


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

## Match API Endpoints

| HTTP Method | URL | Description | Request Body | Response Code | Example Response |
|------------|-----|------------|--------------|---------------|-----------------|
| GET | `/api/v1/matches` | Get all matches with pagination | — | 200 OK | `{ "content": [...], "totalElements": 10, "totalPages": 1 }` |
| POST | `/api/v1/matches` | Create a new match | `MatchRequestDto` | 201 Created | `{ "id": 1, "homeTeam": "Team A", "awayTeam": "Team B", "status": "SCHEDULED" }` |
| POST | `/api/v1/matches/{id}/finish` | Finish a match | `MatchFinishRequestDto` | 200 OK | `{ "id": 1, "status": "FINISHED", "score": "2-1" }` |
| PUT | `/api/v1/matches/{id}` | Update match details | `MatchUpdateRequestDto` | 200 OK | `{ "id": 1, "homeTeam": "Team A", "awayTeam": "Team B", "status": "SCHEDULED" }` |
| DELETE | `/api/v1/matches/{id}` | Delete a match | — | 204 No Content | — |

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
