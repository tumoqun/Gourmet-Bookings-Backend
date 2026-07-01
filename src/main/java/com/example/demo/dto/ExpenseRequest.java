package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {

  private String name;

  private BigDecimal amount;

  private String notes;

  private Long assignmentId;

  private LocalDate expenseDate;

  private LocalTime expenseTime;

  private String submittedBy;

  private String imageUrl;
}
