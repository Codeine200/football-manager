package org.example.footballmanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.footballmanager.entity.Team;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamTournamentStats {
    Team team;
    String season;
    Integer played;
    Integer wins;
    Integer draws;
    Integer losses;
    Integer goalsFor;
    Integer goalsAgainst;
    Integer goalDifference;
    Integer points;
}
