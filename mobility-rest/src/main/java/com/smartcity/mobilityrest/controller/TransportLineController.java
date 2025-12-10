package com.smartcity.mobilityrest.controller;

import com.smartcity.mobilityrest.model.TransportLine;
import com.smartcity.mobilityrest.service.TransportLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lines")
public class TransportLineController {

    private final TransportLineService service;

    public TransportLineController(TransportLineService service) {
        this.service = service;
    }

    @GetMapping
    public List<TransportLine> list(@RequestParam(required = false) String mode) {
        if (mode != null) return service.getByMode(mode);
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportLine> get(@PathVariable Long id) {
        return service.getAll().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TransportLine create(@RequestBody TransportLine line) {
        return service.create(line);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportLine> update(@PathVariable Long id, @RequestBody TransportLine line) {
        return service.getAll().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .map(existing -> {
                    existing.setName(line.getName());
                    existing.setMode(line.getMode());
                    existing.setRoute(line.getRoute());
                    return ResponseEntity.ok(service.create(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.getAll().removeIf(l -> l.getId().equals(id));
    }
}
