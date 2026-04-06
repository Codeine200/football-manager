package org.example.footballmanager.dto.response;

import java.util.List;

public record TeamResponseDto(
        Long id,
        String name,
        String imageUrl,
        List<Player> players
) {
    public record Player(
            String fullName,
            String photo
    ) {}
}