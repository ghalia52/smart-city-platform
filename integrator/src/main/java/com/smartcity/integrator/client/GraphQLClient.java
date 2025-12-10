package com.smartcity.integrator.client;

import com.smartcity.integrator.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class GraphQLClient {
    
    private final WebClient webClient;
    
    public GraphQLClient(@Value("${graphql.service.url}") String graphqlUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(graphqlUrl)
                .build();
    }
    
    public List<UserDTO> getAllUsers() {
        String query = "{ allUsers { id username email createdAt } }";
        return executeQuery(query, "allUsers", UserDTO.class);
    }
    
    public List<TransportLineDTO> getAllTransportLines() {
        String query = "{ allTransportLines { id name mode route } }";
        return executeQuery(query, "allTransportLines", TransportLineDTO.class);
    }
    
    public List<AirQualityDTO> getLatestAirQuality(int limit) {
        String query = String.format(
            "{ latestAirQuality(limit: %d) { id zone aqi pm25 no2 co2 o3 measuredAt } }", 
            limit
        );
        return executeQuery(query, "latestAirQuality", AirQualityDTO.class);
    }
    
    public List<IncidentDTO> getRecentIncidents(int limit) {
        String query = String.format(
            "{ recentIncidents(limit: %d) { id type description lat lon status createdAt } }",
            limit
        );
        return executeQuery(query, "recentIncidents", IncidentDTO.class);
    }
    
    @SuppressWarnings("unchecked")
    private <T> List<T> executeQuery(String query, String fieldName, Class<T> clazz) {
        Map<String, Object> requestBody = Map.of("query", query);
        
        Mono<Map> response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class);
        
        Map<String, Object> result = response.block();
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        
        return (List<T>) data.get(fieldName);
    }
}