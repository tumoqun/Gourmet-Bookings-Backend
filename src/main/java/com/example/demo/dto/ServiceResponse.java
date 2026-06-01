package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    
    private Long id;
    private AreaResponse area;
    private ServiceTypeResponse serviceType;
    private String name;
    private Boolean isPrivateAvailable;
    private Boolean isActive;
    private Integer durationMinutes;
}
