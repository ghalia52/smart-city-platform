package com.smartcity.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Allowed origins - frontend and microservices
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://client-web:3000",
                "http://localhost:8081",
                "http://localhost:8082",
                "http://localhost:8083",
                "http://localhost:8084",
                "http://localhost:8085",
                "http://mobility-rest:8081",
                "http://air-soap:8082",
                "http://emergency-grpc:8083",
                "http://query-graphql:8084",
                "http://integrator:8085"
        ));
        
        // Allowed methods
        corsConfig.setAllowedMethods(Arrays.asList(
                "GET", 
                "POST", 
                "PUT", 
                "DELETE", 
                "OPTIONS", 
                "HEAD", 
                "PATCH"
        ));
        
        // Allow all headers
        corsConfig.setAllowedHeaders(List.of("*"));
        
        // Expose all headers
        corsConfig.setExposedHeaders(List.of("*"));
        
        // Allow credentials
        corsConfig.setAllowCredentials(true);
        
        // Cache preflight response for 1 hour
        corsConfig.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}