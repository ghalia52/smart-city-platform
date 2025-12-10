package com.smartcity.airsoap.service;

import com.smartcity.airsoap.model.AirQuality;
import com.smartcity.airsoap.repository.AirQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirQualityService {
    @Autowired
    private AirQualityRepository repository;

    public int getAqi(String zone) {
        return repository.findFirstByZoneIgnoreCase(zone)
                .map(AirQuality::getAqi)
                .orElse(50); // default if missing
    }

    public String getStatus(int aqi) {
        if (aqi <= 50) return "Good";
        if (aqi <= 100) return "Moderate";
        return "Unhealthy";
    }
}
