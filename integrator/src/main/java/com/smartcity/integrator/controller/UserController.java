package com.smartcity.integrator.controller;

import com.smartcity.integrator.client.GraphQLClient;
import com.smartcity.integrator.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private GraphQLClient graphQLClient;
    
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return graphQLClient.getAllUsers();
    }
}