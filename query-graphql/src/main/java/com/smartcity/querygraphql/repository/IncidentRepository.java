package com.smartcity.querygraphql.repository;

import com.smartcity.querygraphql.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByType(String type);
    List<Incident> findByStatus(String status);
    List<Incident> findAllByOrderByCreatedAtDesc();
}