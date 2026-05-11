package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceRequest {
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    private String serviceNameSnapshot;
    
    @NotNull(message = "Area ID is required")
    private Long areaId;
    
    @NotNull(message = "Service type ID is required")
    private Long serviceTypeId;
    
    private LocalDate targetDate;
    private LocalTime startTime;
    private String timeSlotCode;
    private Boolean isPrivate = false;
    private String timezone;
}
