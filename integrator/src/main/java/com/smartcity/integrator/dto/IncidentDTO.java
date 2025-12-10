package com.smartcity.integrator.dto;

import lombok.Data;

@Data
public class IncidentDTO {
    private Long id;
    private String type;
    private String description;
    private Double lat;
    private Double lon;
    private String status;
    private String createdAt;
}