package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkResponse {
  private Long id;
  private Long orderId;
  private String workNumber;
  private String status;
  private String tourDate;
  private String tourStartTime;
  private String tourEndTime;
  private String tourStartedAt;
  private String tourEndedAt;
  private String closedAt;
  private String locationName;
  private String locationAddress;
  private String notes;
}
