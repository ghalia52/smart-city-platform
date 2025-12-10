package com.smartcity.mobilityrest.repository;

import com.smartcity.mobilityrest.model.TransportLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransportLineRepository extends JpaRepository<TransportLine, Long> {
    List<TransportLine> findByModeIgnoreCase(String mode);
}
