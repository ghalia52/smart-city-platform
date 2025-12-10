package com.smartcity.emergencygrpc.repository;

import com.smartcity.emergencygrpc.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    List<Incident> findByType(String type);
    
    List<Incident> findByStatus(String status);
    
    List<Incident> findByTypeAndStatus(String type, String status);
    
    List<Incident> findAllByOrderByCreatedAtDesc();
}