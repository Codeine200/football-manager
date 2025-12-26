package org.example.footballmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TeamRequestDto (
        @NotBlank(message = "Name must not be blank")
        String name
) {}
