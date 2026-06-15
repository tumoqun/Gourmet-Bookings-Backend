package com.example.demo.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateItineraryStopRequest {

  @NotNull
  private Long workId;

  @NotNull
  private Long serviceId;

  private Long supplierId;

  @NotNull
  private String stopType;

  private LocalTime scheduledTime;

  private String specialNotes;

  private String status;

  private String addedBy;

  private String otherName;
}