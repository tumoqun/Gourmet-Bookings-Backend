package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReceiptRequest {
  private Long supplierId;

  private Long itineraryStopId;

  private BigDecimal amount;

  private BigDecimal fee;

  private BigDecimal tax;

  private Boolean checkNumber;

  private Boolean isVerified;

  private String notes;

  private Long verifiedById;

  private String imageUrl;
}
