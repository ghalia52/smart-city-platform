package com.smartcity.airsoap.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "air_quality")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirQuality {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String zone;
    
    private Integer aqi;
    
    @Column(name = "measured_at")
    private LocalDateTime measuredAt;
}