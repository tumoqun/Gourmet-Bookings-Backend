package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public interface AssignmentListProjection {
  Long getAssignmentId();

  String getAssignmentStatus();

  Long getWorkId();

  String getWorkStatus();

  LocalDate getTourDate();

  LocalTime getTourStartTime();

  LocalTime getTourEndTime();

  Long getDurationMinutes();

  String getServiceName();

  Integer getTotalAdultCount();

  Integer getTotalChildCount();

  BigDecimal getTotalFeeAmount();

  String getResellerName();

  Integer getOrderCount();
}
