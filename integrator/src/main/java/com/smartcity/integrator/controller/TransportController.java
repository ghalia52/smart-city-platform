package com.smartcity.integrator.controller;

import com.smartcity.integrator.client.GraphQLClient;
import com.smartcity.integrator.dto.TransportLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
@CrossOrigin(origins = "*")
public class TransportController {
    
    @Autowired
    private GraphQLClient graphQLClient;
    
    @GetMapping("/lines")
    public List<TransportLineDTO> getAllLines() {
        return graphQLClient.getAllTransportLines();
    }
}