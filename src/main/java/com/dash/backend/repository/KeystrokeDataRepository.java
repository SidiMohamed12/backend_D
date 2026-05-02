package com.dash.backend.repository;

import com.dash.backend.model.KeystrokeData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeystrokeDataRepository extends JpaRepository<KeystrokeData, Long> {
}
