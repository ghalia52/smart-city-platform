package com.smartcity.integrator.dto;

import lombok.Data;

@Data
public class AirQualityDTO {
    private Long id;
    private String zone;
    private Integer aqi;
    private Double pm25;
    private Double no2;
    private Double co2;
    private Double o3;
    private String measuredAt;
}