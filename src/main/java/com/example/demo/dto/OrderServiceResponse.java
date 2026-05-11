package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceResponse {
    
    private Long id;
    private ServiceResponse service;
    private String serviceNameSnapshot;
    private AreaResponse area;
    private ServiceTypeResponse serviceType;
    private LocalDate targetDate;
    private LocalTime startTime;
    private String timeSlotCode;
    private Boolean isPrivate;
    private String timezone;
}
