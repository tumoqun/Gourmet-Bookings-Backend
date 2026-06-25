package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WorkDetailForGuideProjection {
  Long getWorkId();

  String getStatus();

  LocalDate getTourDate();

  LocalTime getTourStartTime();

  LocalTime getTourEndTime();

  Long getDurationMinutes();

  String getLocationAddress();

  String getAgentName();

  String getResellerName();

  String getRef1();

  String getRef2();

  String getServiceName();
}
