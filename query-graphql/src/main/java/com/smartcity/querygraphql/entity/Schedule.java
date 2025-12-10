package com.smartcity.querygraphql.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "schedule")
@Data
@NoArgsConstructor
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "line_id")
    private Long lineId;
    
    @Column(length = 150)
    private String stop;
    
    @Column(name = "departure_time")
    private LocalTime departureTime;
    
    @Column(name = "arrival_time")
    private LocalTime arrivalTime;
    
    @Column(name = "day_of_week", length = 20)
    private String dayOfWeek; // e.g., "Mon-Fri"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", insertable = false, updatable = false)
    private TransportLine transportLine;
}