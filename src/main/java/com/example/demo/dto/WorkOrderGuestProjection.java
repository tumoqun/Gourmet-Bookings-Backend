package com.example.demo.dto;

public interface WorkOrderGuestProjection {
  Long getOrderId();

  Integer getAdultCount();

  Integer getChildCount();

  String getGuestGroupNotes();

  String getLeaderPhone();

  Double getAverageAge();
}
