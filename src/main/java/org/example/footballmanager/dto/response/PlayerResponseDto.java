package org.example.footballmanager.dto.response;

public record PlayerResponseDto (
        Long id,
        String firstName,
        String lastName,
        String middleName,
        TeamResponseDto team
) {
}
