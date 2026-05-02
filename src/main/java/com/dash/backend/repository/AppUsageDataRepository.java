package com.dash.backend.repository;

import com.dash.backend.model.AppUsageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUsageDataRepository extends JpaRepository<AppUsageData, Long> {
}
