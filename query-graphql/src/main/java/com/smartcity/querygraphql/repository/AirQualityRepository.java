package com.smartcity.querygraphql.repository;

import com.smartcity.querygraphql.entity.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AirQualityRepository extends JpaRepository<AirQuality, Long> {
    List<AirQuality> findByZone(String zone);
    
    @Query("SELECT a FROM AirQuality a ORDER BY a.measuredAt DESC")
    List<AirQuality> findLatestReadings();
    
    @Query("SELECT a FROM AirQuality a WHERE a.aqi > :threshold ORDER BY a.measuredAt DESC")
    List<AirQuality> findByAqiGreaterThan(Integer threshold);
}