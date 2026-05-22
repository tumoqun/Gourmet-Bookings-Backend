package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllotmentResponse {

    private Long id;
    private Long serviceId;
    private LocalDate serviceDate;
    private LocalTime startTime;
    private Integer capacityTotal;
    private Integer reservedTotal;
    private Integer availableTotal;
    private String status;
}
