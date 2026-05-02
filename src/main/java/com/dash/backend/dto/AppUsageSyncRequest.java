package com.dash.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record AppUsageSyncRequest(
    @NotBlank String childExternalId,
    @NotBlank String deviceId,
    @NotBlank String deviceName,
    @NotBlank String payload,
    @NotNull OffsetDateTime capturedAt
) {
}
