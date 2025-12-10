package com.smartcity.querygraphql.repository;

import com.smartcity.querygraphql.entity.TransportLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransportLineRepository extends JpaRepository<TransportLine, Long> {
    List<TransportLine> findByMode(String mode);
    List<TransportLine> findByNameContainingIgnoreCase(String name);
}