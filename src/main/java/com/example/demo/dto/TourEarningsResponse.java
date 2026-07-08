package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourEarningsResponse {
    private LocalDate tourDate;
    private LocalTime tourStartTime;
    private LocalTime tourEndTime;
    private String serviceName;

    private BigDecimal hoursEarned;
    private BigDecimal hourlySalary;
    private BigDecimal taxableSalary;

    private BigDecimal travelExpenses;
    private BigDecimal receiptsForTour;
}
