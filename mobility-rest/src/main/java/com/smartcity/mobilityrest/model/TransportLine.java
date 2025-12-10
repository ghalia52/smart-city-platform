package com.smartcity.mobilityrest.model;

import jakarta.persistence.*;

@Entity
public class TransportLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mode; // bus/train/metro
    private String route;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }
}
