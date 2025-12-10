package com.smartcity.mobilityrest.service;

import com.smartcity.mobilityrest.model.TransportLine;
import com.smartcity.mobilityrest.repository.TransportLineRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransportLineService {

    private final TransportLineRepository repository;

    public TransportLineService(TransportLineRepository repository) {
        this.repository = repository;
    }

    public List<TransportLine> getAll() {
        return repository.findAll();
    }

    public List<TransportLine> getByMode(String mode) {
        return repository.findByModeIgnoreCase(mode);
    }

    public TransportLine create(TransportLine line) {
        return repository.save(line);
    }
}
