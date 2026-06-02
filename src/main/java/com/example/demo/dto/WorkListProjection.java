package com.example.demo.dto;

import java.time.LocalDate;

public interface WorkListProjection {
    Long getId();
    String getWorkNumber();
    String getStatus();
    LocalDate getTourDate();
}
