package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateExpenseRequest {
  private String name;

  private BigDecimal amount;

  private String notes;

  private Long assignmentId;

  private String imageUrl;
}
