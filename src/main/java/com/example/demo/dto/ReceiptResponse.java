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
public class ReceiptResponse {
  private Long id;

  private Long assignmentId;

  private Long supplierId;

  private String supplierName;

  private String supplierType;

  private Long itineraryStopId;

  private BigDecimal amount;

  private BigDecimal fee;

  private BigDecimal tax;

  private LocalDate receiptDate;

  private LocalTime receiptTime;

  private String notes;

  private String imageUrl;

  private Boolean checkNumber;

  private Boolean isVerified;

}
