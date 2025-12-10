package com.smartcity.querygraphql.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "transport_line")
@Data
@NoArgsConstructor
public class TransportLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 50)
    private String mode; // bus/train/metro
    
    @Column(columnDefinition = "TEXT")
    private String route;
    
    @OneToMany(mappedBy = "transportLine", fetch = FetchType.LAZY)
    private List<Schedule> schedules;
}