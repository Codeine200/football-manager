package org.example.footballmanager.domain;

public record TeamId(Long id) {

    public TeamId {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Team id must be positive");
        }
    }
}