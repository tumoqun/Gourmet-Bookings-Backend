package com.example.demo.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface WorkItineraryStopList {
  Long getId();

  Long getItineraryId();

  Long getSupplierId();

  String getSupplierName();

  String getSupplierPhone();

  Integer getStopSequence();

  String getStopType();

  LocalTime getScheduledTime();

  Integer getEstimatedDurationMinutes();

  String getDescription();

  String getSpecialNotes();

  String getStatus();

  String getAddedBy();

  LocalDateTime getCreatedAt();
}
