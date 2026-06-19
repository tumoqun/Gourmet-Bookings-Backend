package com.example.demo.dto;

import java.time.LocalDate;
import java.time.Duration;
import java.time.LocalTime;

public interface WorkDetailProjection {
  Long getId();
  String getWorkNumber();
  String getStatus();
  LocalDate getTourDate();
  LocalTime getTourStartTime();
  LocalTime getTourEndTime();
  Long getOrderId();
  Integer getAdultCount();
  Integer getChildCount();
  Boolean getIsPrivate();
  Long getAreaId();
  String getAreaName();
  Integer getServiceId();
  String getServiceName();
  String getLocationName();
  String getLocationAddress();
  String getNotes();
  default Double getDuration() {
    if (getTourStartTime() == null || getTourEndTime() == null) {
      return null;
    }
    return Duration.between(
        getTourStartTime(),
        getTourEndTime()).toMinutes() / 60.0;
  }
}
