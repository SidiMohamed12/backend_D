package com.dash.backend.repository;

import com.dash.backend.model.ChildDevice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildDeviceRepository extends JpaRepository<ChildDevice, Long> {

    Optional<ChildDevice> findByExternalId(String externalId);
}
