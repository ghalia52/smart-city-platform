package com.smartcity.integrator.config;

import com.smartcity.emergencygrpc.grpc.EmergencyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {
    
    @Value("${grpc.client.emergency-service.address:static://emergency-grpc:9090}")
    private String grpcAddress;
    
    @Bean
    public ManagedChannel grpcChannel() {
        // Extract host and port from address
        String address = grpcAddress.replace("static://", "");
        String[] parts = address.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }
    
    @Bean
    public EmergencyServiceGrpc.EmergencyServiceBlockingStub emergencyServiceStub(ManagedChannel channel) {
        return EmergencyServiceGrpc.newBlockingStub(channel);
    }
}