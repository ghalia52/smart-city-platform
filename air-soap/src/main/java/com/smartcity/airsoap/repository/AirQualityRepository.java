package com.smartcity.airsoap.repository;

import com.smartcity.airsoap.model.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AirQualityRepository extends JpaRepository<AirQuality, Long> {
    Optional<AirQuality> findFirstByZoneIgnoreCase(String zone);
}
