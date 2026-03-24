package org.example.footballmanager.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Player {

    private final String fullName;
    private final TeamId teamId;

    @Builder
    public Player(String fullName, TeamId teamId) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name must not be blank");
        }
        if (teamId == null) {
            throw new IllegalArgumentException("Team must not be null");
        }
        this.fullName = fullName;
        this.teamId = teamId;
    }
}

