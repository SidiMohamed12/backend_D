package com.dash.backend.controller;

import com.dash.backend.dto.AppUsageSyncRequest;
import com.dash.backend.dto.KeystrokeSyncRequest;
import com.dash.backend.dto.LocationSyncRequest;
import com.dash.backend.service.SyncService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sync")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/keystrokes")
    public ResponseEntity<Map<String, String>> syncKeystrokes(@Valid @RequestBody KeystrokeSyncRequest request) {
        syncService.saveKeystroke(request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/app-usage")
    public ResponseEntity<Map<String, String>> syncAppUsage(@Valid @RequestBody AppUsageSyncRequest request) {
        syncService.saveAppUsage(request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/locations")
    public ResponseEntity<Map<String, String>> syncLocation(@Valid @RequestBody LocationSyncRequest request) {
        syncService.saveLocation(request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
