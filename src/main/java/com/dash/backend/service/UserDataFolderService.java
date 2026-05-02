package com.dash.backend.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserDataFolderService {

    private static final DateTimeFormatter FILE_TS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss", Locale.ROOT);

    private final Path basePath;

    public UserDataFolderService(@Value("${storage.base-dir:data/users}") String baseDir) {
        this.basePath = Path.of(baseDir).toAbsolutePath().normalize();
    }

    public void ensureUserFolders(String deviceId, String deviceName) {
        String folderName = buildFolderName(deviceId, deviceName);
        try {
            Files.createDirectories(basePath.resolve(folderName).resolve("messages"));
            Files.createDirectories(basePath.resolve(folderName).resolve("images"));
            Files.createDirectories(basePath.resolve(folderName).resolve("videos"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create user directories", e);
        }
    }

    public void ensureUserFolders(String userExternalId) {
        ensureUserFolders(userExternalId, "");
    }

    public String storeMessage(String userExternalId, String content, OffsetDateTime capturedAt) {
        String folderName = buildFolderName(userExternalId, "");
        OffsetDateTime effectiveTs = capturedAt == null ? OffsetDateTime.now(ZoneOffset.UTC) : capturedAt;
        String fileName = "msg_" + FILE_TS_FORMAT.format(effectiveTs) + "_" + UUID.randomUUID() + ".txt";
        Path target = basePath.resolve(folderName).resolve("messages").resolve(fileName);

        ensureUserFolders(userExternalId);
        String body = "capturedAt=" + effectiveTs + System.lineSeparator() + content + System.lineSeparator();
        try {
            Files.writeString(target, body, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store message", e);
        }

        return basePath.relativize(target).toString().replace('\\', '/');
    }

    public String storeImage(String userExternalId, MultipartFile file) {
        return storeBinary(buildFolderName(userExternalId, ""), file, "images", "img");
    }

    public String storeVideo(String userExternalId, MultipartFile file) {
        return storeBinary(buildFolderName(userExternalId, ""), file, "videos", "vid");
    }

    private String storeBinary(String folderName, MultipartFile file, String folder, String prefix) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String extension = getExtension(file.getOriginalFilename());
        String fileName = prefix + "_" + FILE_TS_FORMAT.format(OffsetDateTime.now(ZoneOffset.UTC)) + "_" + UUID.randomUUID() + extension;
        Path target = basePath.resolve(folderName).resolve(folder).resolve(fileName);

        try {
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file", e);
        }

        return basePath.relativize(target).toString().replace('\\', '/');
    }

    private String buildFolderName(String deviceId, String deviceName) {
        String safeId   = sanitize(deviceId);
        String safeName = sanitize(deviceName);
        if (safeName.isBlank()) {
            return safeId;
        }
        return safeId + "_" + safeName;
    }

    private String sanitize(String raw) {
        String safe = raw == null ? "unknown" : raw.replaceAll("[^a-zA-Z0-9_-]", "_");
        if (safe.isBlank()) {
            return "unknown";
        }
        return safe;
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            return ".bin";
        }

        int index = originalFilename.lastIndexOf('.');
        if (index == -1 || index == originalFilename.length() - 1) {
            return ".bin";
        }

        String ext = originalFilename.substring(index).toLowerCase(Locale.ROOT);
        return ext.length() > 10 ? ".bin" : ext;
    }
}
