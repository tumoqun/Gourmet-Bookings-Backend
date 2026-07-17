package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface WorkDetailForGuideProjection {
  Long getWorkId();

  String getStatus();

  LocalDate getTourDate();

  LocalTime getTourStartTime();

  LocalTime getTourEndTime();

  Long getDurationMinutes();

  Integer getServiceDurationMinutes();

  Boolean getIsPrivateAvailable();

  Integer getAdultCount();

  Integer getChildCount();

  LocalDateTime getTourStartedAt();

  LocalDateTime getTourEndedAt();

  String getLocationAddress();

  String getAgentName();

  String getResellerName();

  String getRef1();

  String getRef2();

  String getServiceName();
}
