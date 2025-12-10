package com.smartcity.emergencygrpc.service;

import com.smartcity.emergencygrpc.entity.Incident;
import com.smartcity.emergencygrpc.repository.IncidentRepository;
import com.smartcity.emergencygrpc.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GrpcService
public class EmergencyGrpcService extends EmergencyServiceGrpc.EmergencyServiceImplBase {
    
    @Autowired
    private IncidentRepository repository;
    
    @Override
    public void reportIncident(IncidentRequest request, StreamObserver<IncidentResponse> responseObserver) {
        Incident incident = new Incident(
            request.getType(),
            request.getDescription(),
            request.getLat(),
            request.getLon()
        );
        
        Incident saved = repository.save(incident);
        
        IncidentResponse response = IncidentResponse.newBuilder()
                .setStatus("success")
                .setIncidentId(saved.getId())
                .setMessage("Incident reported successfully with ID: " + saved.getId())
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getAllIncidents(Empty request, StreamObserver<IncidentList> responseObserver) {
        List<Incident> incidents = repository.findAllByOrderByCreatedAtDesc();
        
        List<com.smartcity.emergencygrpc.grpc.Incident> grpcIncidents = incidents.stream()
                .map(this::convertToGrpcIncident)
                .collect(Collectors.toList());
        
        IncidentList response = IncidentList.newBuilder()
                .addAllIncidents(grpcIncidents)
                .setTotal(incidents.size())
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getIncidentsByType(TypeRequest request, StreamObserver<IncidentList> responseObserver) {
        List<Incident> incidents = repository.findByType(request.getType());
        
        List<com.smartcity.emergencygrpc.grpc.Incident> grpcIncidents = incidents.stream()
                .map(this::convertToGrpcIncident)
                .collect(Collectors.toList());
        
        IncidentList response = IncidentList.newBuilder()
                .addAllIncidents(grpcIncidents)
                .setTotal(incidents.size())
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void getIncidentsByStatus(StatusRequest request, StreamObserver<IncidentList> responseObserver) {
        List<Incident> incidents = repository.findByStatus(request.getStatus());
        
        List<com.smartcity.emergencygrpc.grpc.Incident> grpcIncidents = incidents.stream()
                .map(this::convertToGrpcIncident)
                .collect(Collectors.toList());
        
        IncidentList response = IncidentList.newBuilder()
                .addAllIncidents(grpcIncidents)
                .setTotal(incidents.size())
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void updateIncidentStatus(StatusUpdateRequest request, StreamObserver<IncidentResponse> responseObserver) {
        Optional<Incident> optionalIncident = repository.findById(request.getIncidentId());
        
        if (optionalIncident.isPresent()) {
            Incident incident = optionalIncident.get();
            incident.setStatus(request.getStatus());
            repository.save(incident);
            
            IncidentResponse response = IncidentResponse.newBuilder()
                    .setStatus("success")
                    .setIncidentId(incident.getId())
                    .setMessage("Incident status updated to: " + request.getStatus())
                    .build();
            
            responseObserver.onNext(response);
        } else {
            IncidentResponse response = IncidentResponse.newBuilder()
                    .setStatus("error")
                    .setIncidentId(request.getIncidentId())
                    .setMessage("Incident not found")
                    .build();
            
            responseObserver.onNext(response);
        }
        
        responseObserver.onCompleted();
    }
    
    private com.smartcity.emergencygrpc.grpc.Incident convertToGrpcIncident(Incident incident) {
        return com.smartcity.emergencygrpc.grpc.Incident.newBuilder()
                .setId(incident.getId())
                .setType(incident.getType())
                .setDescription(incident.getDescription() != null ? incident.getDescription() : "")
                .setLat(incident.getLat() != null ? incident.getLat() : 0.0)
                .setLon(incident.getLon() != null ? incident.getLon() : 0.0)
                .setStatus(incident.getStatus() != null ? incident.getStatus() : "new")
                .setCreatedAt(incident.getCreatedAt() != null ? incident.getCreatedAt().toString() : "")
                .build();
    }
}