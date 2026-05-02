package com.dash.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record StoreMessageRequest(
    @NotBlank String content,
    @NotNull OffsetDateTime capturedAt
) {
}
