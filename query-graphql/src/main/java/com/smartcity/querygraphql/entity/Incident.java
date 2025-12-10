package com.smartcity.querygraphql.entity;

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
    
    @Column(length = 80)
    private String type; // accident, fire, ambulance
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private Double lat;
    
    private Double lon;
    
    @Column(length = 30)
    private String status; // default 'new'
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}