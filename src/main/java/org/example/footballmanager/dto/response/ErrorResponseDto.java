package org.example.footballmanager.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDto(int status, String message, Map<String, String> fieldErrors, LocalDateTime timestamp) {
    public ErrorResponseDto(int status, String message) {
        this(status, message, null, LocalDateTime.now());
    }

    public ErrorResponseDto(int status, String message, Map<String, String> fieldErrors) {
        this(status, message, fieldErrors, LocalDateTime.now());
    }
}