package org.example.footballmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamTournamentStats {
    TeamData team;
    Integer season;
    Integer played = 0;
    Integer wins = 0;
    Integer draws = 0;
    Integer losses = 0;
    Integer goalsFor = 0;
    Integer goalsAgainst = 0;
    Integer goalDifference = 0;
    Integer points = 0;
}
