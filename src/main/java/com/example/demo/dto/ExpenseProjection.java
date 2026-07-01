package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface ExpenseProjection {
  Long getId();

  BigDecimal getAmount();

  LocalDate getExpenseDate();

  LocalTime getExpenseTime();

  String getNotes();

  String getImageUrl();

  Long getAssignmentId();

  String getName();

  LocalDateTime getCreatedAt();

  String getSubmittedBy();
}
