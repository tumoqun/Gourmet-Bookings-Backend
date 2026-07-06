package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Native-query projection for the main accounting list query.
 * Returns one row per work, with the first order / first service
 * resolved via correlated subqueries.
 */
public interface AccountingRowProjection {

  Long getWorkId();

  String getWorkNumber();

  String getWorkStatus();

  LocalDate getTourDate();

  LocalTime getTourStartTime();

  String getNotes();

  /* First order */
  String getRef1();

  String getResellerName();

  Boolean getIsPrivate();

  Integer getAdultCount();

  Integer getChildCount();

  /* First service */
  String getServiceName();
}
