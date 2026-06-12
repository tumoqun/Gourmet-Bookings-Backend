package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateItineraryStopResponse {

  private Long id;

  private Long itineraryId;

  private Long supplierId;

  private String stopType;

  private String status;
}