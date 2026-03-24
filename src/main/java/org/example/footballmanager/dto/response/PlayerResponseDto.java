package org.example.footballmanager.dto.response;

public record PlayerResponseDto (
        Long id,
        String fullName,
        String photo,
        TeamResponseDto team
) {
}
