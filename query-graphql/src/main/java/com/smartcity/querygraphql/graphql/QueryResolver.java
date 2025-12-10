package com.smartcity.querygraphql.graphql;

import com.smartcity.querygraphql.entity.*;
import com.smartcity.querygraphql.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class QueryResolver {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransportLineRepository transportLineRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private AirQualityRepository airQualityRepository;
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    // ============ USER QUERIES ============
    
    @QueryMapping
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    
    @QueryMapping
    public User user(@Argument Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @QueryMapping
    public User userByUsername(@Argument String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    // ============ TRANSPORT QUERIES ============
    
    @QueryMapping
    public List<TransportLine> allTransportLines() {
        return transportLineRepository.findAll();
    }
    
    @QueryMapping
    public TransportLine transportLine(@Argument Long id) {
        return transportLineRepository.findById(id).orElse(null);
    }
    
    @QueryMapping
    public List<TransportLine> transportLinesByMode(@Argument String mode) {
        return transportLineRepository.findByMode(mode);
    }
    
    @SchemaMapping(typeName = "TransportLine", field = "schedules")
    public List<Schedule> transportLineSchedules(TransportLine transportLine) {
        return scheduleRepository.findByLineId(transportLine.getId());
    }
    
    // ============ SCHEDULE QUERIES ============
    
    @QueryMapping
    public List<Schedule> allSchedules() {
        return scheduleRepository.findAll();
    }
    
    @QueryMapping
    public Schedule schedule(@Argument Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }
    
    @QueryMapping
    public List<Schedule> schedulesByLine(@Argument Long lineId) {
        return scheduleRepository.findByLineId(lineId);
    }
    
    @QueryMapping
    public List<Schedule> schedulesByStop(@Argument String stop) {
        return scheduleRepository.findByStop(stop);
    }
    
    @QueryMapping
    public List<Schedule> schedulesByDay(@Argument String dayOfWeek) {
        return scheduleRepository.findByDayOfWeek(dayOfWeek);
    }
    
    // ============ AIR QUALITY QUERIES ============
    
    @QueryMapping
    public List<AirQuality> allAirQuality() {
        return airQualityRepository.findAll();
    }
    
    @QueryMapping
    public AirQuality airQuality(@Argument Long id) {
        return airQualityRepository.findById(id).orElse(null);
    }
    
    @QueryMapping
    public List<AirQuality> airQualityByZone(@Argument String zone) {
        return airQualityRepository.findByZone(zone);
    }
    
    @QueryMapping
    public List<AirQuality> latestAirQuality(@Argument Integer limit) {
        List<AirQuality> all = airQualityRepository.findLatestReadings();
        return all.stream().limit(limit != null ? limit : 10).toList();
    }
    
    @QueryMapping
    public List<AirQuality> poorAirQuality(@Argument Integer threshold) {
        return airQualityRepository.findByAqiGreaterThan(threshold != null ? threshold : 100);
    }
    
    // ============ INCIDENT QUERIES ============
    
    @QueryMapping
    public List<Incident> allIncidents() {
        return incidentRepository.findAll();
    }
    
    @QueryMapping
    public Incident incident(@Argument Long id) {
        return incidentRepository.findById(id).orElse(null);
    }
    
    @QueryMapping
    public List<Incident> incidentsByType(@Argument String type) {
        return incidentRepository.findByType(type);
    }
    
    @QueryMapping
    public List<Incident> incidentsByStatus(@Argument String status) {
        return incidentRepository.findByStatus(status);
    }
    
    @QueryMapping
    public List<Incident> recentIncidents(@Argument Integer limit) {
        List<Incident> all = incidentRepository.findAllByOrderByCreatedAtDesc();
        return all.stream().limit(limit != null ? limit : 10).toList();
    }
    
    // ============ USER MUTATIONS ============
    
    @MutationMapping
    public User createUser(@Argument UserInput input) {
        User user = new User();
        user.setUsername(input.username());
        user.setEmail(input.email());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @MutationMapping
    public User updateUser(@Argument Long id, @Argument UserInput input) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(input.username());
        user.setEmail(input.email());
        return userRepository.save(user);
    }
    
    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userRepository.deleteById(id);
        return true;
    }
    
    // ============ TRANSPORT MUTATIONS ============
    
    @MutationMapping
    public TransportLine createTransportLine(@Argument TransportLineInput input) {
        TransportLine line = new TransportLine();
        line.setName(input.name());
        line.setMode(input.mode());
        line.setRoute(input.route());
        return transportLineRepository.save(line);
    }
    
    @MutationMapping
    public TransportLine updateTransportLine(@Argument Long id, @Argument TransportLineInput input) {
        TransportLine line = transportLineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport line not found"));
        line.setName(input.name());
        line.setMode(input.mode());
        line.setRoute(input.route());
        return transportLineRepository.save(line);
    }
    
    @MutationMapping
    public Boolean deleteTransportLine(@Argument Long id) {
        transportLineRepository.deleteById(id);
        return true;
    }
    
    // ============ SCHEDULE MUTATIONS ============
    
    @MutationMapping
    public Schedule createSchedule(@Argument ScheduleInput input) {
        Schedule schedule = new Schedule();
        schedule.setLineId(input.lineId());
        schedule.setStop(input.stop());
        schedule.setDepartureTime(LocalTime.parse(input.departureTime()));
        schedule.setArrivalTime(LocalTime.parse(input.arrivalTime()));
        schedule.setDayOfWeek(input.dayOfWeek());
        return scheduleRepository.save(schedule);
    }
    
    @MutationMapping
    public Schedule updateSchedule(@Argument Long id, @Argument ScheduleInput input) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setLineId(input.lineId());
        schedule.setStop(input.stop());
        schedule.setDepartureTime(LocalTime.parse(input.departureTime()));
        schedule.setArrivalTime(LocalTime.parse(input.arrivalTime()));
        schedule.setDayOfWeek(input.dayOfWeek());
        return scheduleRepository.save(schedule);
    }
    
    @MutationMapping
    public Boolean deleteSchedule(@Argument Long id) {
        scheduleRepository.deleteById(id);
        return true;
    }
    
    // ============ AIR QUALITY MUTATIONS ============
    
    @MutationMapping
    public AirQuality reportAirQuality(@Argument AirQualityInput input) {
        AirQuality airQuality = new AirQuality();
        airQuality.setZone(input.zone());
        airQuality.setAqi(input.aqi());
        airQuality.setPm25(input.pm25());
        airQuality.setNo2(input.no2());
        airQuality.setCo2(input.co2());
        airQuality.setO3(input.o3());
        airQuality.setMeasuredAt(LocalDateTime.now());
        return airQualityRepository.save(airQuality);
    }
    
    // ============ INCIDENT MUTATIONS ============
    
    @MutationMapping
    public Incident reportIncident(@Argument IncidentInput input) {
        Incident incident = new Incident();
        incident.setType(input.type());
        incident.setDescription(input.description());
        incident.setLat(input.lat());
        incident.setLon(input.lon());
        incident.setStatus("new");
        incident.setCreatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }
    
    @MutationMapping
    public Incident updateIncidentStatus(@Argument Long id, @Argument String status) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }
}

// Input Records
record UserInput(String username, String email) {}
record TransportLineInput(String name, String mode, String route) {}
record ScheduleInput(Long lineId, String stop, String departureTime, String arrivalTime, String dayOfWeek) {}
record AirQualityInput(String zone, Integer aqi, Double pm25, Double no2, Double co2, Double o3) {}
record IncidentInput(String type, String description, Double lat, Double lon) {}