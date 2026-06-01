package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkResponse {
  private Long id;
  private List<Long> orderIds;
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
