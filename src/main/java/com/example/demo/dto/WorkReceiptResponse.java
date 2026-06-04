package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkReceiptResponse {
  private Long id;

  private Long assignmentId;

  private Long supplierId;

  private String supplierName;

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

  private Boolean isVerified;

  private String submittedBy;

  private LocalDateTime createdAt;
}
