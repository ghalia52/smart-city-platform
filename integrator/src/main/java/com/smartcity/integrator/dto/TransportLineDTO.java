package com.smartcity.integrator.dto;

import lombok.Data;
import java.util.List;

@Data
public class TransportLineDTO {
    private Long id;
    private String name;
    private String mode;
    private String route;
    private List<ScheduleDTO> schedules;
}

@Data
class ScheduleDTO {
    private Long id;
    private String stop;
    private String departureTime;
    private String arrivalTime;
    private String dayOfWeek;
}