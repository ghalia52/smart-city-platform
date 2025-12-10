package com.smartcity.integrator.controller;

import com.smartcity.emergencygrpc.grpc.*;
import com.smartcity.integrator.dto.IncidentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;  // ← ADD THIS IMPORT
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
    
    @Autowired
    private EmergencyServiceGrpc.EmergencyServiceBlockingStub emergencyStub;
    
    @PostMapping
    public IncidentDTO reportIncident(@RequestBody IncidentDTO dto) {
        IncidentRequest request = IncidentRequest.newBuilder()
                .setType(dto.getType())
                .setDescription(dto.getDescription())
                .setLat(dto.getLat())
                .setLon(dto.getLon())
                .build();
        
        IncidentResponse response = emergencyStub.reportIncident(request);
        
        dto.setId(response.getIncidentId());
        dto.setStatus("new");
        return dto;
    }
    
    @GetMapping
    public List<IncidentDTO> getAllIncidents() {
        IncidentList response = emergencyStub.getAllIncidents(Empty.newBuilder().build());
        
        return response.getIncidentsList().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/type/{type}")
    public List<IncidentDTO> getByType(@PathVariable String type) {
        TypeRequest request = TypeRequest.newBuilder().setType(type).build();
        IncidentList response = emergencyStub.getIncidentsByType(request);
        
        return response.getIncidentsList().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @PatchMapping("/{id}/status")
    public IncidentDTO updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        StatusUpdateRequest request = StatusUpdateRequest.newBuilder()
                .setIncidentId(id)
                .setStatus(body.get("status"))
                .build();
        
        emergencyStub.updateIncidentStatus(request);
        
        IncidentDTO dto = new IncidentDTO();
        dto.setId(id);
        dto.setStatus(body.get("status"));
        return dto;
    }
    
    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setId(incident.getId());
        dto.setType(incident.getType());
        dto.setDescription(incident.getDescription());
        dto.setLat(incident.getLat());
        dto.setLon(incident.getLon());
        dto.setStatus(incident.getStatus());
        dto.setCreatedAt(incident.getCreatedAt());
        return dto;
    }
}