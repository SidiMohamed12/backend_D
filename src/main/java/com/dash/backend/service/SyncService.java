package com.dash.backend.service;

import com.dash.backend.dto.AppUsageSyncRequest;
import com.dash.backend.dto.KeystrokeSyncRequest;
import com.dash.backend.dto.LocationSyncRequest;
import com.dash.backend.model.AppUsageData;
import com.dash.backend.model.ChildDevice;
import com.dash.backend.model.KeystrokeData;
import com.dash.backend.model.LocationData;
import com.dash.backend.repository.AppUsageDataRepository;
import com.dash.backend.repository.ChildDeviceRepository;
import com.dash.backend.repository.KeystrokeDataRepository;
import com.dash.backend.repository.LocationDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SyncService {

    private final ChildDeviceRepository childDeviceRepository;
    private final KeystrokeDataRepository keystrokeDataRepository;
    private final AppUsageDataRepository appUsageDataRepository;
    private final LocationDataRepository locationDataRepository;
    private final UserDataFolderService userDataFolderService;

    public SyncService(
        ChildDeviceRepository childDeviceRepository,
        KeystrokeDataRepository keystrokeDataRepository,
        AppUsageDataRepository appUsageDataRepository,
        LocationDataRepository locationDataRepository,
        UserDataFolderService userDataFolderService
    ) {
        this.childDeviceRepository = childDeviceRepository;
        this.keystrokeDataRepository = keystrokeDataRepository;
        this.appUsageDataRepository = appUsageDataRepository;
        this.locationDataRepository = locationDataRepository;
        this.userDataFolderService = userDataFolderService;
    }

    @Transactional
    public void saveKeystroke(KeystrokeSyncRequest request) {
        ChildDevice childDevice = getOrCreateChild(request.childExternalId(), request.deviceId(), request.deviceName());
        KeystrokeData data = new KeystrokeData();
        data.setChildDevice(childDevice);
        data.setPayload(request.payload());
        data.setCapturedAt(request.capturedAt());
        keystrokeDataRepository.save(data);
    }

    @Transactional
    public void saveAppUsage(AppUsageSyncRequest request) {
        ChildDevice childDevice = getOrCreateChild(request.childExternalId(), request.deviceId(), request.deviceName());
        AppUsageData data = new AppUsageData();
        data.setChildDevice(childDevice);
        data.setPayload(request.payload());
        data.setCapturedAt(request.capturedAt());
        appUsageDataRepository.save(data);
    }

    @Transactional
    public void saveLocation(LocationSyncRequest request) {
        ChildDevice childDevice = getOrCreateChild(request.childExternalId(), request.deviceId(), request.deviceName());
        LocationData data = new LocationData();
        data.setChildDevice(childDevice);
        data.setLatitude(request.latitude());
        data.setLongitude(request.longitude());
        data.setCapturedAt(request.capturedAt());
        locationDataRepository.save(data);
    }

    private ChildDevice getOrCreateChild(String externalId, String deviceId, String deviceName) {
        ChildDevice childDevice = childDeviceRepository.findByExternalId(externalId)
            .orElseGet(() -> {
                ChildDevice cd = new ChildDevice();
                cd.setExternalId(externalId);
                cd.setLabel("child-" + externalId);
                cd.setDeviceId(deviceId);
                cd.setDeviceName(deviceName);
                return childDeviceRepository.save(cd);
            });

        userDataFolderService.ensureUserFolders(deviceId, deviceName);
        return childDevice;
    }
}
