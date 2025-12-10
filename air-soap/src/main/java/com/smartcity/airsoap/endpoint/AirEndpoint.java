package com.smartcity.airsoap.endpoint;

import com.smartcity.airsoap.service.AirQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.*;

import smartcity.com.air.GetAqiRequest;
import smartcity.com.air.GetAqiResponse;

@Endpoint
public class AirEndpoint {
    private static final String NAMESPACE = "http://smartcity.com/air";

    @Autowired
    private AirQualityService service;

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetAqiRequest")
    @ResponsePayload
    public GetAqiResponse getAqi(@RequestPayload GetAqiRequest request) {
        int aqi = service.getAqi(request.getZone());
        String status = service.getStatus(aqi);

        GetAqiResponse response = new GetAqiResponse();
        response.setZone(request.getZone());
        response.setAqi(aqi);
        response.setStatus(status);
        return response;
    }
}
