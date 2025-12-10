package com.smartcity.emergencygrpc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
public class Incident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String type; // accident, fire, ambulance
    
    private String description;
    
    private Double lat;
    
    private Double lon;
    
    private String status; // new, in-progress, resolved
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public Incident(String type, String description, Double lat, Double lon) {
        this.type = type;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.status = "new";
        this.createdAt = LocalDateTime.now();
    }
}