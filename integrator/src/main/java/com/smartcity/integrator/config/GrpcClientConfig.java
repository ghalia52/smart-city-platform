package com.smartcity.integrator.config;

import com.smartcity.emergencygrpc.grpc.EmergencyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {
    
    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }
    
    @Bean
    public EmergencyServiceGrpc.EmergencyServiceBlockingStub emergencyServiceStub(ManagedChannel channel) {
        return EmergencyServiceGrpc.newBlockingStub(channel);
    }
}