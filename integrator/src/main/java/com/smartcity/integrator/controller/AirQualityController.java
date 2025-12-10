package com.smartcity.integrator.controller;

import com.smartcity.integrator.client.GraphQLClient;
import com.smartcity.integrator.dto.AirQualityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/air-quality")
public class AirQualityController {
    
    @Autowired
    private GraphQLClient graphQLClient;
    
    @GetMapping("/latest")
    public List<AirQualityDTO> getLatest(@RequestParam(defaultValue = "10") int limit) {
        return graphQLClient.getLatestAirQuality(limit);
    }
}