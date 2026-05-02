package com.dash.backend.controller;

import com.dash.backend.dto.StoreMessageRequest;
import com.dash.backend.service.UserDataFolderService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UserDataController {

    private final UserDataFolderService userDataFolderService;

    public UserDataController(UserDataFolderService userDataFolderService) {
        this.userDataFolderService = userDataFolderService;
    }

    @PostMapping("/{userId}/messages")
    public ResponseEntity<Map<String, String>> storeMessage(
        @PathVariable String userId,
        @Valid @RequestBody StoreMessageRequest request
    ) {
        String path = userDataFolderService.storeMessage(userId, request.content(), request.capturedAt());
        return ResponseEntity.ok(Map.of("status", "ok", "path", path));
    }

    @PostMapping(value = "/{userId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> storeImage(
        @PathVariable String userId,
        @RequestPart("file") MultipartFile file
    ) {
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "file must be an image"));
        }

        String path = userDataFolderService.storeImage(userId, file);
        return ResponseEntity.ok(Map.of("status", "ok", "path", path));
    }

    @PostMapping(value = "/{userId}/videos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> storeVideo(
        @PathVariable String userId,
        @RequestPart("file") MultipartFile file
    ) {
        if (file.getContentType() == null || !file.getContentType().startsWith("video/")) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "file must be a video"));
        }

        String path = userDataFolderService.storeVideo(userId, file);
        return ResponseEntity.ok(Map.of("status", "ok", "path", path));
    }
}
