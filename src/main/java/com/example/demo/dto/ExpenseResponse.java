package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {

  private Long id;

  private String name;

  private BigDecimal amount;

  private String notes;

  private String imageUrl;

  private Long assignmentId;

  private LocalDate expenseDate;

  private LocalTime expenseTime;

  private String submittedBy;
}
