package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public interface WorkOrderForGuideProjection {
  Long getOrderId();

  String getContactName();

  String getRef1();

  String getRef2();

  Boolean getIsPrivate();

  Integer getAdultCount();

  Integer getChildCount();

  BigDecimal getTotalFeeAmount();

  String getStatus();

  String getNotes();

  LocalDate getTourDate();

  LocalTime getTourStartTime();

  LocalTime getTourEndTime();

  String getServiceName();
}
