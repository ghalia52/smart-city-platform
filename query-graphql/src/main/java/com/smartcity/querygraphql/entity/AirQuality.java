package com.smartcity.querygraphql.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "air_quality")
@Data
@NoArgsConstructor
public class AirQuality {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String zone;
    
    private Integer aqi;
    
    private Double pm25;
    
    private Double no2;
    
    private Double co2;
    
    private Double o3;
    
    @Column(name = "measured_at")
    private LocalDateTime measuredAt;
}