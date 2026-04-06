package org.example.footballmanager.domain;

import lombok.Data;

@Data
public class TeamData {
    private final Long id;
    private final String name;
    private final String imageUrl;
}
