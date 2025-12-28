package org.example.footballmanager.domain;

import lombok.Getter;

@Getter
public class Team {
    private final TeamId teamId;
    private final boolean guest;

    public Team(TeamId teamId, boolean guest) {
        if (teamId == null) {
            throw new IllegalArgumentException("TeamId must not be null");
        }
        this.teamId = teamId;
        this.guest = guest;
    }
}
