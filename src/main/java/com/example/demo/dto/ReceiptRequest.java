package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRequest {
  static {
    System.out.println("ReceiptRequest VERSION 2026-06-29");
  }

  @NotNull
  private Long assignmentId;
  @NotNull
  private Long supplierId;
  @NotNull
  private Long itineraryStopId;

  private String receiptType;

  private String description;

  private BigDecimal amount;

  private BigDecimal fee;

  private BigDecimal tax;

  private String currencyCode;

  private LocalDate receiptDate;

  private LocalTime receiptTime;

  private String receiptNumber;

  private String category;

  private String paymentMethod;

  private String notes;

  private String imageUrl;

  private Boolean checkNumber;

  private Boolean isVerified;

  private Long verifiedById;

  private LocalDateTime verifiedAt;

  private String submittedBy;
}
